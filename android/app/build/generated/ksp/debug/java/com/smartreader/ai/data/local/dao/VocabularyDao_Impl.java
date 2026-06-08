package com.smartreader.ai.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.smartreader.ai.data.local.entity.VocabularyEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class VocabularyDao_Impl implements VocabularyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VocabularyEntity> __insertionAdapterOfVocabularyEntity;

  private final EntityDeletionOrUpdateAdapter<VocabularyEntity> __deletionAdapterOfVocabularyEntity;

  private final EntityUpsertionAdapter<VocabularyEntity> __upsertionAdapterOfVocabularyEntity;

  public VocabularyDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVocabularyEntity = new EntityInsertionAdapter<VocabularyEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `vocabulary_words` (`id`,`word`,`meaning`,`example`,`sourceBookId`,`dateLearned`,`timesReviewed`,`mastered`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VocabularyEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getWord());
        statement.bindString(3, entity.getMeaning());
        statement.bindString(4, entity.getExample());
        if (entity.getSourceBookId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSourceBookId());
        }
        statement.bindLong(6, entity.getDateLearned());
        statement.bindLong(7, entity.getTimesReviewed());
        final int _tmp = entity.getMastered() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
    this.__deletionAdapterOfVocabularyEntity = new EntityDeletionOrUpdateAdapter<VocabularyEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `vocabulary_words` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VocabularyEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__upsertionAdapterOfVocabularyEntity = new EntityUpsertionAdapter<VocabularyEntity>(new EntityInsertionAdapter<VocabularyEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `vocabulary_words` (`id`,`word`,`meaning`,`example`,`sourceBookId`,`dateLearned`,`timesReviewed`,`mastered`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VocabularyEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getWord());
        statement.bindString(3, entity.getMeaning());
        statement.bindString(4, entity.getExample());
        if (entity.getSourceBookId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSourceBookId());
        }
        statement.bindLong(6, entity.getDateLearned());
        statement.bindLong(7, entity.getTimesReviewed());
        final int _tmp = entity.getMastered() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    }, new EntityDeletionOrUpdateAdapter<VocabularyEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `vocabulary_words` SET `id` = ?,`word` = ?,`meaning` = ?,`example` = ?,`sourceBookId` = ?,`dateLearned` = ?,`timesReviewed` = ?,`mastered` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VocabularyEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getWord());
        statement.bindString(3, entity.getMeaning());
        statement.bindString(4, entity.getExample());
        if (entity.getSourceBookId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getSourceBookId());
        }
        statement.bindLong(6, entity.getDateLearned());
        statement.bindLong(7, entity.getTimesReviewed());
        final int _tmp = entity.getMastered() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getId());
      }
    });
  }

  @Override
  public Object insert(final VocabularyEntity word, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfVocabularyEntity.insert(word);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final VocabularyEntity word, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfVocabularyEntity.handle(word);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsert(final VocabularyEntity word, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfVocabularyEntity.upsert(word);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<VocabularyEntity>> observeAll() {
    final String _sql = "SELECT * FROM vocabulary_words ORDER BY dateLearned DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vocabulary_words"}, new Callable<List<VocabularyEntity>>() {
      @Override
      @NonNull
      public List<VocabularyEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
          final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
          final int _cursorIndexOfSourceBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceBookId");
          final int _cursorIndexOfDateLearned = CursorUtil.getColumnIndexOrThrow(_cursor, "dateLearned");
          final int _cursorIndexOfTimesReviewed = CursorUtil.getColumnIndexOrThrow(_cursor, "timesReviewed");
          final int _cursorIndexOfMastered = CursorUtil.getColumnIndexOrThrow(_cursor, "mastered");
          final List<VocabularyEntity> _result = new ArrayList<VocabularyEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpWord;
            _tmpWord = _cursor.getString(_cursorIndexOfWord);
            final String _tmpMeaning;
            _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
            final String _tmpExample;
            _tmpExample = _cursor.getString(_cursorIndexOfExample);
            final String _tmpSourceBookId;
            if (_cursor.isNull(_cursorIndexOfSourceBookId)) {
              _tmpSourceBookId = null;
            } else {
              _tmpSourceBookId = _cursor.getString(_cursorIndexOfSourceBookId);
            }
            final long _tmpDateLearned;
            _tmpDateLearned = _cursor.getLong(_cursorIndexOfDateLearned);
            final int _tmpTimesReviewed;
            _tmpTimesReviewed = _cursor.getInt(_cursorIndexOfTimesReviewed);
            final boolean _tmpMastered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfMastered);
            _tmpMastered = _tmp != 0;
            _item = new VocabularyEntity(_tmpId,_tmpWord,_tmpMeaning,_tmpExample,_tmpSourceBookId,_tmpDateLearned,_tmpTimesReviewed,_tmpMastered);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<VocabularyEntity>> observeForReview() {
    final String _sql = "SELECT * FROM vocabulary_words WHERE mastered = 0 ORDER BY timesReviewed ASC, dateLearned ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vocabulary_words"}, new Callable<List<VocabularyEntity>>() {
      @Override
      @NonNull
      public List<VocabularyEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWord = CursorUtil.getColumnIndexOrThrow(_cursor, "word");
          final int _cursorIndexOfMeaning = CursorUtil.getColumnIndexOrThrow(_cursor, "meaning");
          final int _cursorIndexOfExample = CursorUtil.getColumnIndexOrThrow(_cursor, "example");
          final int _cursorIndexOfSourceBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceBookId");
          final int _cursorIndexOfDateLearned = CursorUtil.getColumnIndexOrThrow(_cursor, "dateLearned");
          final int _cursorIndexOfTimesReviewed = CursorUtil.getColumnIndexOrThrow(_cursor, "timesReviewed");
          final int _cursorIndexOfMastered = CursorUtil.getColumnIndexOrThrow(_cursor, "mastered");
          final List<VocabularyEntity> _result = new ArrayList<VocabularyEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpWord;
            _tmpWord = _cursor.getString(_cursorIndexOfWord);
            final String _tmpMeaning;
            _tmpMeaning = _cursor.getString(_cursorIndexOfMeaning);
            final String _tmpExample;
            _tmpExample = _cursor.getString(_cursorIndexOfExample);
            final String _tmpSourceBookId;
            if (_cursor.isNull(_cursorIndexOfSourceBookId)) {
              _tmpSourceBookId = null;
            } else {
              _tmpSourceBookId = _cursor.getString(_cursorIndexOfSourceBookId);
            }
            final long _tmpDateLearned;
            _tmpDateLearned = _cursor.getLong(_cursorIndexOfDateLearned);
            final int _tmpTimesReviewed;
            _tmpTimesReviewed = _cursor.getInt(_cursorIndexOfTimesReviewed);
            final boolean _tmpMastered;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfMastered);
            _tmpMastered = _tmp != 0;
            _item = new VocabularyEntity(_tmpId,_tmpWord,_tmpMeaning,_tmpExample,_tmpSourceBookId,_tmpDateLearned,_tmpTimesReviewed,_tmpMastered);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> count() {
    final String _sql = "SELECT COUNT(*) FROM vocabulary_words";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vocabulary_words"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
