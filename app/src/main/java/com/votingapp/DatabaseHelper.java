package com.votingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.votingapp.models.Option;
import com.votingapp.models.Poll;
import com.votingapp.models.Question;
import com.votingapp.models.Referendum;
import com.votingapp.models.User;
import com.votingapp.models.Voting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Votings.db";

    SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {}


    public void insertUser(User user){
        ContentValues values = new ContentValues();
        values.put(User.COLUMN_USERNAME, user.getUserName());
        values.put(User.COLUMN_PASSWORD, user.getPassword());
        values.put(User.COLUMN_ISADMIN, (user.isAdmin() ? 1 : 0));
        sqLiteDatabase.insert(User.TABLE_NAME, "", values) ;
    }

    public ArrayList<User> selectAllUsers(){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM users", null);
        ArrayList<User> usersList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1);
                usersList.add(user);
            } while (cursor.moveToNext());
        }

        for(User user : usersList){
            System.out.println(user.getUserName() + ", " + user.getPassword() + ", " + user.isAdmin());
        }

        return usersList;
    }

    private long insertQuestion(Question question){
        ContentValues values = new ContentValues();
        values.put(Question.COLUMN_QUESTION_TEXT, question.getQuestionText());
        return sqLiteDatabase.insert(Question.TABLE_NAME, "", values) ;
    }

    private Question selectQuestionById(int id){
        ContentValues values = new ContentValues();
        values.put(Question.COLUMN_ID, id);
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT questionText FROM questions WHERE questions.id = " + id, null);
        cursor.moveToFirst();
        String questionText = cursor.getString(0);
        cursor.close();
        return new Question(questionText);
    }

    private int insertOption(Option option){
        ContentValues values = new ContentValues();
        values.put(Option.COLUMN_OPTION_TEXT, option.getOptionText());
        values.put(Option.COLUMN_TIMES_SELECTED, option.getTimesSelected());
        long optionId = sqLiteDatabase.insert(Option.TABLE_NAME, "", values);

        checkIfInsertIsSuccessful(optionId);

        return (int) optionId;
    }

    public void selectAllOptions(){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM poll_questions", null);
        System.out.println(" \n\n\n= SELECT ALL OPTIONS = \n" + cursorToString(cursor));
    }

    public void insertReferendum(Referendum referendum) {
        long insertedId = insertQuestion(referendum.getQuestion());
        checkIfInsertIsSuccessful(insertedId);

        ContentValues values = new ContentValues();
        values.put(Referendum.COLUMN_TITLE, referendum.getTitle());
        values.put(Referendum.COLUMN_QUESTION_ID, (int) insertedId);
        values.put(Referendum.COLUMN_YES_SELECTED_TIMES, referendum.getOptionYes().getTimesSelected());
        values.put(Referendum.COLUMN_NO_SELECTED_TIMES, referendum.getOptionNo().getTimesSelected());
        sqLiteDatabase.insert(Referendum.TABLE_NAME, "", values) ;
    }

    public ArrayList<Referendum> selectAllReferendums(){
        Cursor cursor = sqLiteDatabase.rawQuery(
        "SELECT referendums.id, title, questionText, yesSelectedTimes, noSelectedTimes FROM referendums"
            + " INNER JOIN questions"
            + " ON referendums.questionId = questions.id", null);
        ArrayList<Referendum> referendumsList = new ArrayList<>();
        System.out.println(cursorToString(cursor));


        if (cursor.moveToFirst()) {
            do {
                Referendum referendum = new Referendum(cursor.getInt(0),
                        cursor.getString(1), new Question(cursor.getString(2)));
                referendum.getOptionYes().setTimesSelected(cursor.getInt(3));
                referendum.getOptionNo().setTimesSelected(cursor.getInt(4));
                referendumsList.add(referendum);
            } while (cursor.moveToNext());
        }

        return referendumsList;
    }

    public void updateReferendum(Referendum referendum){
        ContentValues cv = new ContentValues();
        cv.put(Referendum.COLUMN_YES_SELECTED_TIMES, referendum.getOptionYes().getTimesSelected());
        cv.put(Referendum.COLUMN_NO_SELECTED_TIMES, referendum.getOptionNo().getTimesSelected());
        sqLiteDatabase.update(Referendum.TABLE_NAME, cv, "id=" + referendum.getId(), null);
    }

    public void insertVoting(Voting voting) {
        long insertedId = insertQuestion(voting.getQuestion());
        checkIfInsertIsSuccessful(insertedId);

        ContentValues values = new ContentValues();
        values.put(Voting.COLUMN_TITLE, voting.getTitle());
        values.put(Voting.COLUMN_QUESTION_ID, (int) insertedId);
        long votingId = sqLiteDatabase.insert(Voting.TABLE_NAME_VOTINGS, "", values);

        checkIfInsertIsSuccessful(votingId);

        for(Option option : voting.getOptions()){
            int optionId = insertOption(option);
            values = new ContentValues();
            values.put(Voting.COLUMN_VOTING_ID, votingId);
            values.put(Voting.COLUMN_OPTION_ID, optionId);
            long votingOptionId = sqLiteDatabase.insert(Voting.TABLE_NAME_VOTING_OPTIONS, "", values);

            checkIfInsertIsSuccessful(votingOptionId);
        }
    }

    public ArrayList<Voting> selectAllVotings(){
        ArrayList<Voting> votingsList = new ArrayList<>();

        Cursor votingCursor = sqLiteDatabase.rawQuery("SELECT id, title, questionId FROM votings", null);
        System.out.println(" = SELECT VOTING = \n" + cursorToString(votingCursor));

        if (votingCursor.moveToFirst()) {
            do {
                int votingId = votingCursor.getInt(0);
                String votingTitle = votingCursor.getString(1);
                int questionId = votingCursor.getInt(2);

                Question question = selectQuestionById(questionId);

                Cursor optionCursor = sqLiteDatabase.rawQuery(
                    "SELECT options.id, options.optionText, options.timesSelected"
                    + " FROM voting_options"
                    + " INNER JOIN options"
                    + " ON voting_options.optionId = options.id"
                    + " WHERE voting_options.votingId = " + votingId, null);

                System.out.println(" = SELECT VOTING_OPTIONS = \n" + cursorToString(optionCursor));

                ArrayList<Option> optionList = new ArrayList<>();
                if (optionCursor.moveToFirst()) {
                    do {
                        Option option = new Option(optionCursor.getInt(0), optionCursor.getString(1));
                        option.setTimesSelected(optionCursor.getInt(2));
                        optionList.add(option);
                    } while (optionCursor.moveToNext());
                }
                votingsList.add(new Voting(votingTitle,question,optionList));
            } while (votingCursor.moveToNext());
        }
        return votingsList;
    }

    public void updateOption(Option option){
        ContentValues cv = new ContentValues();
        cv.put(Option.COLUMN_TIMES_SELECTED, option.getTimesSelected());
        sqLiteDatabase.update(Option.TABLE_NAME, cv, "id=" + option.getId(), null);
    }

    public void insertPoll(Poll poll){
        ContentValues values = new ContentValues();
        values.put(Poll.COLUMN_TITLE, poll.getTitle());
        long pollId = sqLiteDatabase.insert(Poll.TABLE_NAME_POLLS, "", values);
        checkIfInsertIsSuccessful(pollId);

        for (Map.Entry<Question, ArrayList<Option>> entry : poll.getPollContent().entrySet()) {
            long insertedQuestionId = insertQuestion(entry.getKey());
            checkIfInsertIsSuccessful(insertedQuestionId);

            values = new ContentValues();
            values.put(Poll.COLUMN_POLL_ID, pollId);
            values.put(Poll.COLUMN_QUESTION_ID, insertedQuestionId);
            long pollQuestionId = sqLiteDatabase.insert(Poll.TABLE_NAME_POLL_QUESTIONS, "", values);
            checkIfInsertIsSuccessful(pollQuestionId);

            for(Option option : entry.getValue()) {
                long insertedOptionId = insertOption(option);
                checkIfInsertIsSuccessful(insertedOptionId);

                values = new ContentValues();
                values.put(Poll.COLUMN_POLL_QUESTION_ID, insertedQuestionId);
                values.put(Poll.COLUMN_QUESTION_OPTION_ID, insertedOptionId);
                long questionOptionId = sqLiteDatabase.insert(Poll.TABLE_NAME_POLL_QUESTION_OPTIONS, "", values);
                checkIfInsertIsSuccessful(questionOptionId);
            }

        }
    }

    public ArrayList<Poll> selectAllPolls(){
        ArrayList<Poll> pollsList = new ArrayList<>();
        HashMap<Question, ArrayList<Option>> questionsAndOptions = new HashMap<>();

        Cursor pollCursor = sqLiteDatabase.rawQuery("SELECT id, title FROM polls", null);
        System.out.println(" = SELECT POLL = \n" + cursorToString(pollCursor));

        if (pollCursor.moveToFirst()) {
            do {
                int pollId = pollCursor.getInt(0);
                String pollTitle = pollCursor.getString(1);

                Cursor questionCursor = sqLiteDatabase.rawQuery("SELECT questionId, questionText " +
                        "FROM poll_questions " +
                        "INNER JOIN questions " +
                        "ON questions.id = poll_questions.questionId " +
                        "WHERE pollId = " + pollId, null);
                System.out.println(" = SELECT POLL_QUESTIONS = \n" + cursorToString(questionCursor));

                if (questionCursor.moveToFirst()) {
                    do {
                        int questionId = questionCursor.getInt(0);
                        System.out.println("QUESTION ID = " + questionId);
                        String questionText = questionCursor.getString(1);
                        ArrayList<Option> options = new ArrayList<>();

                        Question question = new Question(questionText);

                        Cursor optionCursor = sqLiteDatabase.rawQuery("SELECT options.id, optionText, timesSelected " +
                                "FROM poll_question_options " +
                                "INNER JOIN options " +
                                "ON options.id = poll_question_options.questionOptionId " +
                                "WHERE poll_question_options.pollQuestionId = " + questionId, null);
                        System.out.println(" = SELECT POLL_QUESTION_OPTIONS = \n" + cursorToString(optionCursor));
                        if (optionCursor.moveToFirst()) {
                            do {
                                int optionId = optionCursor.getInt(0);
                                String optionText = optionCursor.getString(1);
                                int timesSelected = optionCursor.getInt(2);
                                Option option = new Option(optionId, optionText);
                                option.setTimesSelected(timesSelected);
                                options.add(option);
                           }while (optionCursor.moveToNext());
                        }
                        questionsAndOptions.put(question,options);
                    }while(questionCursor.moveToNext());
                }
                pollsList.add(new Poll(pollTitle,new HashMap<>(questionsAndOptions)));
                questionsAndOptions.clear();
            } while (pollCursor.moveToNext());
        }
        return pollsList;
    }

    private void checkIfInsertIsSuccessful(long id){
        if(id == -1){
            try {
                throw new Exception("Error when inserting in database...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createAllTables(){
        sqLiteDatabase.execSQL(User.CREATE_TABLE);
        sqLiteDatabase.execSQL(Question.CREATE_TABLE);
        sqLiteDatabase.execSQL(Option.CREATE_TABLE);
        sqLiteDatabase.execSQL(Voting.CREATE_TABLE_VOTINGS);
        sqLiteDatabase.execSQL(Voting.CREATE_TABLE_VOTING_OPTIONS);
        sqLiteDatabase.execSQL(Referendum.CREATE_TABLE);
        sqLiteDatabase.execSQL(Poll.CREATE_TABLE_POLLS);
        sqLiteDatabase.execSQL(Poll.CREATE_TABLE_POLL_QUESTIONS);
        sqLiteDatabase.execSQL(Poll.CREATE_TABLE_POLL_QUESTION_OPTIONS);

    }

    public String cursorToString(Cursor cursor){
        String cursorString = "";
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            for (String name: columnNames)
                cursorString += String.format("%s ][ ", name);
            cursorString += "\n";
            do {
                for (String name: columnNames) {
                    cursorString += String.format("%s ][ ",
                            cursor.getString(cursor.getColumnIndex(name)));
                }
                cursorString += "\n";
            } while (cursor.moveToNext());
        }
        return cursorString;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

    public void setUpDatabase(){
        if(!checkIfTableExists(User.TABLE_NAME)) sqLiteDatabase.execSQL(User.CREATE_TABLE);
        if(!checkIfTableExists(Question.TABLE_NAME)) sqLiteDatabase.execSQL(Question.CREATE_TABLE);
        if(!checkIfTableExists(Option.TABLE_NAME)) sqLiteDatabase.execSQL(Option.CREATE_TABLE);
        if(!checkIfTableExists(Voting.TABLE_NAME_VOTINGS)) sqLiteDatabase.execSQL(Voting.CREATE_TABLE_VOTINGS);
        if(!checkIfTableExists(Voting.TABLE_NAME_VOTING_OPTIONS)) sqLiteDatabase.execSQL(Voting.CREATE_TABLE_VOTING_OPTIONS);
        if(!checkIfTableExists(Referendum.TABLE_NAME)) sqLiteDatabase.execSQL(Referendum.CREATE_TABLE);
        if(!checkIfTableExists(Poll.TABLE_NAME_POLLS)) sqLiteDatabase.execSQL(Poll.CREATE_TABLE_POLLS);
        if(!checkIfTableExists(Poll.TABLE_NAME_POLL_QUESTIONS)) sqLiteDatabase.execSQL(Poll.CREATE_TABLE_POLL_QUESTIONS);
        if(!checkIfTableExists(Poll.TABLE_NAME_POLL_QUESTION_OPTIONS)) sqLiteDatabase.execSQL(Poll.CREATE_TABLE_POLL_QUESTION_OPTIONS);

        if(checkIfTableIsEmpty(User.TABLE_NAME)) insertDefaultUsers();
        if(checkIfTableIsEmpty(Voting.TABLE_NAME_VOTINGS)) insertDefaultVotings();
        if(checkIfTableIsEmpty(Poll.TABLE_NAME_POLLS)) insertDefaultPolls();
        if(checkIfTableIsEmpty(Referendum.TABLE_NAME)) insertDefaultReferendums();
    }

    private boolean checkIfTableIsEmpty(String table){
        String sqlQuery = "SELECT count(*) FROM '" + table + "'";
        Cursor mcursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        mcursor.moveToFirst();
        int count = mcursor.getInt(0);
        return count == 0;
    }

    private boolean checkIfTableExists(String table){
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+table+"'";
        Cursor mCursor = sqLiteDatabase.rawQuery(sql, null);
        int count = mCursor.getCount();
        mCursor.close();
        return count > 0; // > 0 means table exists
    }

    private void insertDefaultUsers(){
        User admin = new User("admin", "1234", true);
        User test = new User("test", "1234", false);

        AppController.databaseHelper.insertUser(admin);
        AppController.databaseHelper.insertUser(test);
    }

    private void insertDefaultVotings(){
        ArrayList<Option> voting1Options = new ArrayList<>();
        voting1Options.add(new Option("Георги Иванов"));
        voting1Options.add(new Option("Иван Георгиев"));
        voting1Options.add(new Option("Петър Димитров"));

        voting1Options.get(0).setTimesSelected(5);
        voting1Options.get(1).setTimesSelected(3);
        voting1Options.get(2).setTimesSelected(4);
        Voting voting1 = new Voting("Гласуване за нов президент на компанията", new Question("Кой искате да е новият президент на компанията?"), voting1Options);
        AppController.databaseHelper.insertVoting(voting1);
    }

    private void insertDefaultPolls(){
        ArrayList<Poll> polls = new ArrayList<>();
        ArrayList<Question> poll1Questions = new ArrayList<>();
        poll1Questions.add(new Question("Любим цвят"));
        poll1Questions.add(new Question("Любимо животно"));

        ArrayList<Option> poll1Question1Options = new ArrayList<>();
        poll1Question1Options.add(new Option("Зелен"));
        poll1Question1Options.add(new Option("Червен"));
        poll1Question1Options.add(new Option("Син"));
        poll1Question1Options.add(new Option("Жълт"));

        ArrayList<Option> poll1Question2Options = new ArrayList<>();
        poll1Question2Options.add(new Option("Котка"));
        poll1Question2Options.add(new Option("Куче"));
        poll1Question2Options.add(new Option("Жираф"));
        poll1Question2Options.add(new Option("Гущер"));
        poll1Question2Options.add(new Option("Слон"));

        HashMap<Question, ArrayList<Option>> pollContent = new HashMap<>();
        pollContent.put(poll1Questions.get(0), poll1Question1Options);
        pollContent.put(poll1Questions.get(1), poll1Question2Options);

        Poll poll1 = new Poll("Първа анкета", pollContent);
        AppController.databaseHelper.insertPoll(poll1);
    }

    private void insertDefaultReferendums(){
        ArrayList<Referendum> referendums = new ArrayList<>();
        Referendum referendum = new Referendum("Референдум 2019", new Question("Съгласни ли сте с плана за създаване на нова атомна електроцентрала?"));
        referendum.getOptionYes().setTimesSelected(1);
        referendum.getOptionNo().setTimesSelected(1);
        AppController.databaseHelper.insertReferendum(referendum);
    }
}
