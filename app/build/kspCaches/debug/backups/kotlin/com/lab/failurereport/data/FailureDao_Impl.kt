package com.lab.failurereport.`data`

import androidx.lifecycle.LiveData
import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FailureDao_Impl(
  __db: RoomDatabase,
) : FailureDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFailureReport: EntityInsertAdapter<FailureReport>

  private val __deleteAdapterOfFailureReport: EntityDeleteOrUpdateAdapter<FailureReport>
  init {
    this.__db = __db
    this.__insertAdapterOfFailureReport = object : EntityInsertAdapter<FailureReport>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `failures` (`id`,`location`,`inventoryNumber`,`description`,`photoPath`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FailureReport) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.location)
        statement.bindText(3, entity.inventoryNumber)
        statement.bindText(4, entity.description)
        val _tmpPhotoPath: String? = entity.photoPath
        if (_tmpPhotoPath == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpPhotoPath)
        }
        statement.bindLong(6, entity.timestamp)
      }
    }
    this.__deleteAdapterOfFailureReport = object : EntityDeleteOrUpdateAdapter<FailureReport>() {
      protected override fun createQuery(): String = "DELETE FROM `failures` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: FailureReport) {
        statement.bindLong(1, entity.id)
      }
    }
  }

  public override suspend fun insert(report: FailureReport): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfFailureReport.insertAndReturnId(_connection, report)
    _result
  }

  public override suspend fun delete(report: FailureReport): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfFailureReport.handle(_connection, report)
  }

  public override fun getAll(): LiveData<List<FailureReport>> {
    val _sql: String = "SELECT * FROM failures ORDER BY timestamp DESC"
    return __db.invalidationTracker.createLiveData(arrayOf("failures"), false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfInventoryNumber: Int = getColumnIndexOrThrow(_stmt, "inventoryNumber")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfPhotoPath: Int = getColumnIndexOrThrow(_stmt, "photoPath")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<FailureReport> = mutableListOf()
        while (_stmt.step()) {
          val _item: FailureReport
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpLocation: String
          _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          val _tmpInventoryNumber: String
          _tmpInventoryNumber = _stmt.getText(_columnIndexOfInventoryNumber)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpPhotoPath: String?
          if (_stmt.isNull(_columnIndexOfPhotoPath)) {
            _tmpPhotoPath = null
          } else {
            _tmpPhotoPath = _stmt.getText(_columnIndexOfPhotoPath)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item =
              FailureReport(_tmpId,_tmpLocation,_tmpInventoryNumber,_tmpDescription,_tmpPhotoPath,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: Long): FailureReport? {
    val _sql: String = "SELECT * FROM failures WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLocation: Int = getColumnIndexOrThrow(_stmt, "location")
        val _columnIndexOfInventoryNumber: Int = getColumnIndexOrThrow(_stmt, "inventoryNumber")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfPhotoPath: Int = getColumnIndexOrThrow(_stmt, "photoPath")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: FailureReport?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpLocation: String
          _tmpLocation = _stmt.getText(_columnIndexOfLocation)
          val _tmpInventoryNumber: String
          _tmpInventoryNumber = _stmt.getText(_columnIndexOfInventoryNumber)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpPhotoPath: String?
          if (_stmt.isNull(_columnIndexOfPhotoPath)) {
            _tmpPhotoPath = null
          } else {
            _tmpPhotoPath = _stmt.getText(_columnIndexOfPhotoPath)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _result =
              FailureReport(_tmpId,_tmpLocation,_tmpInventoryNumber,_tmpDescription,_tmpPhotoPath,_tmpTimestamp)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
