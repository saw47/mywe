package app;

import android.app.Application;

import db.NoteRepository;
import db.NoteRoomDatabase;

public class App extends Application {

    public static App instance;

    private NoteRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        repository = new NoteRepository(this);
    }

    public static App getInstance() {
        return instance;
    }

    public NoteRepository getRepository() {
        return repository;
    }
}
