package com.tecsup.cafecampus.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.tecsup.cafecampus.data.local.entity.DetallePedido;
import com.tecsup.cafecampus.data.local.entity.Pedido;
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class PedidoDao_Impl extends PedidoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Pedido> __insertionAdapterOfPedido;

  private final EntityInsertionAdapter<DetallePedido> __insertionAdapterOfDetallePedido;

  private final SharedSQLiteStatement __preparedStmtOfCambiarEstado;

  public PedidoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPedido = new EntityInsertionAdapter<Pedido>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pedidos` (`id`,`clienteId`,`estado`,`horaRecojo`,`pago`,`nota`,`total`,`fecha`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pedido entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getClienteId());
        statement.bindString(3, entity.getEstado());
        statement.bindString(4, entity.getHoraRecojo());
        statement.bindString(5, entity.getPago());
        statement.bindString(6, entity.getNota());
        statement.bindDouble(7, entity.getTotal());
        statement.bindLong(8, entity.getFecha());
      }
    };
    this.__insertionAdapterOfDetallePedido = new EntityInsertionAdapter<DetallePedido>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `detalle_pedido` (`id`,`pedidoId`,`productoId`,`nombreProducto`,`cantidad`,`precioUnitario`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DetallePedido entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPedidoId());
        statement.bindLong(3, entity.getProductoId());
        statement.bindString(4, entity.getNombreProducto());
        statement.bindLong(5, entity.getCantidad());
        statement.bindDouble(6, entity.getPrecioUnitario());
      }
    };
    this.__preparedStmtOfCambiarEstado = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE pedidos SET estado = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertarPedido(final Pedido pedido, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPedido.insertAndReturnId(pedido);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertarDetalles(final List<DetallePedido> detalles,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDetallePedido.insert(detalles);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object crearPedido(final Pedido pedido, final List<DetallePedido> detalles,
      final Continuation<? super Long> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> PedidoDao_Impl.super.crearPedido(pedido, detalles, __cont), $completion);
  }

  @Override
  public Object cambiarEstado(final int id, final String estado,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCambiarEstado.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, estado);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfCambiarEstado.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PedidoConDetalle>> observarPorCliente(final int clienteId) {
    final String _sql = "SELECT * FROM pedidos WHERE clienteId = ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clienteId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"detalle_pedido",
        "pedidos"}, new Callable<List<PedidoConDetalle>>() {
      @Override
      @NonNull
      public List<PedidoConDetalle> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfClienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "clienteId");
            final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
            final int _cursorIndexOfHoraRecojo = CursorUtil.getColumnIndexOrThrow(_cursor, "horaRecojo");
            final int _cursorIndexOfPago = CursorUtil.getColumnIndexOrThrow(_cursor, "pago");
            final int _cursorIndexOfNota = CursorUtil.getColumnIndexOrThrow(_cursor, "nota");
            final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
            final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
            final LongSparseArray<ArrayList<DetallePedido>> _collectionDetalles = new LongSparseArray<ArrayList<DetallePedido>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionDetalles.containsKey(_tmpKey)) {
                _collectionDetalles.put(_tmpKey, new ArrayList<DetallePedido>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipdetallePedidoAscomTecsupCafecampusDataLocalEntityDetallePedido(_collectionDetalles);
            final List<PedidoConDetalle> _result = new ArrayList<PedidoConDetalle>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final PedidoConDetalle _item;
              final Pedido _tmpPedido;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final int _tmpClienteId;
              _tmpClienteId = _cursor.getInt(_cursorIndexOfClienteId);
              final String _tmpEstado;
              _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
              final String _tmpHoraRecojo;
              _tmpHoraRecojo = _cursor.getString(_cursorIndexOfHoraRecojo);
              final String _tmpPago;
              _tmpPago = _cursor.getString(_cursorIndexOfPago);
              final String _tmpNota;
              _tmpNota = _cursor.getString(_cursorIndexOfNota);
              final double _tmpTotal;
              _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
              final long _tmpFecha;
              _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
              _tmpPedido = new Pedido(_tmpId,_tmpClienteId,_tmpEstado,_tmpHoraRecojo,_tmpPago,_tmpNota,_tmpTotal,_tmpFecha);
              final ArrayList<DetallePedido> _tmpDetallesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpDetallesCollection = _collectionDetalles.get(_tmpKey_1);
              _item = new PedidoConDetalle(_tmpPedido,_tmpDetallesCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<PedidoConDetalle>> observarPorEstado(final String estado) {
    final String _sql = "SELECT * FROM pedidos WHERE estado = ? ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, estado);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"detalle_pedido",
        "pedidos"}, new Callable<List<PedidoConDetalle>>() {
      @Override
      @NonNull
      public List<PedidoConDetalle> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfClienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "clienteId");
            final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
            final int _cursorIndexOfHoraRecojo = CursorUtil.getColumnIndexOrThrow(_cursor, "horaRecojo");
            final int _cursorIndexOfPago = CursorUtil.getColumnIndexOrThrow(_cursor, "pago");
            final int _cursorIndexOfNota = CursorUtil.getColumnIndexOrThrow(_cursor, "nota");
            final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
            final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
            final LongSparseArray<ArrayList<DetallePedido>> _collectionDetalles = new LongSparseArray<ArrayList<DetallePedido>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionDetalles.containsKey(_tmpKey)) {
                _collectionDetalles.put(_tmpKey, new ArrayList<DetallePedido>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipdetallePedidoAscomTecsupCafecampusDataLocalEntityDetallePedido(_collectionDetalles);
            final List<PedidoConDetalle> _result = new ArrayList<PedidoConDetalle>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final PedidoConDetalle _item;
              final Pedido _tmpPedido;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final int _tmpClienteId;
              _tmpClienteId = _cursor.getInt(_cursorIndexOfClienteId);
              final String _tmpEstado;
              _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
              final String _tmpHoraRecojo;
              _tmpHoraRecojo = _cursor.getString(_cursorIndexOfHoraRecojo);
              final String _tmpPago;
              _tmpPago = _cursor.getString(_cursorIndexOfPago);
              final String _tmpNota;
              _tmpNota = _cursor.getString(_cursorIndexOfNota);
              final double _tmpTotal;
              _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
              final long _tmpFecha;
              _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
              _tmpPedido = new Pedido(_tmpId,_tmpClienteId,_tmpEstado,_tmpHoraRecojo,_tmpPago,_tmpNota,_tmpTotal,_tmpFecha);
              final ArrayList<DetallePedido> _tmpDetallesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpDetallesCollection = _collectionDetalles.get(_tmpKey_1);
              _item = new PedidoConDetalle(_tmpPedido,_tmpDetallesCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<PedidoConDetalle> observarUno(final int id) {
    final String _sql = "SELECT * FROM pedidos WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"detalle_pedido",
        "pedidos"}, new Callable<PedidoConDetalle>() {
      @Override
      @Nullable
      public PedidoConDetalle call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfClienteId = CursorUtil.getColumnIndexOrThrow(_cursor, "clienteId");
            final int _cursorIndexOfEstado = CursorUtil.getColumnIndexOrThrow(_cursor, "estado");
            final int _cursorIndexOfHoraRecojo = CursorUtil.getColumnIndexOrThrow(_cursor, "horaRecojo");
            final int _cursorIndexOfPago = CursorUtil.getColumnIndexOrThrow(_cursor, "pago");
            final int _cursorIndexOfNota = CursorUtil.getColumnIndexOrThrow(_cursor, "nota");
            final int _cursorIndexOfTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
            final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
            final LongSparseArray<ArrayList<DetallePedido>> _collectionDetalles = new LongSparseArray<ArrayList<DetallePedido>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionDetalles.containsKey(_tmpKey)) {
                _collectionDetalles.put(_tmpKey, new ArrayList<DetallePedido>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipdetallePedidoAscomTecsupCafecampusDataLocalEntityDetallePedido(_collectionDetalles);
            final PedidoConDetalle _result;
            if (_cursor.moveToFirst()) {
              final Pedido _tmpPedido;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final int _tmpClienteId;
              _tmpClienteId = _cursor.getInt(_cursorIndexOfClienteId);
              final String _tmpEstado;
              _tmpEstado = _cursor.getString(_cursorIndexOfEstado);
              final String _tmpHoraRecojo;
              _tmpHoraRecojo = _cursor.getString(_cursorIndexOfHoraRecojo);
              final String _tmpPago;
              _tmpPago = _cursor.getString(_cursorIndexOfPago);
              final String _tmpNota;
              _tmpNota = _cursor.getString(_cursorIndexOfNota);
              final double _tmpTotal;
              _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
              final long _tmpFecha;
              _tmpFecha = _cursor.getLong(_cursorIndexOfFecha);
              _tmpPedido = new Pedido(_tmpId,_tmpClienteId,_tmpEstado,_tmpHoraRecojo,_tmpPago,_tmpNota,_tmpTotal,_tmpFecha);
              final ArrayList<DetallePedido> _tmpDetallesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpDetallesCollection = _collectionDetalles.get(_tmpKey_1);
              _result = new PedidoConDetalle(_tmpPedido,_tmpDetallesCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> ventasDesde(final long inicio) {
    final String _sql = "SELECT COALESCE(SUM(total), 0.0) FROM pedidos WHERE fecha >= ? AND estado != 'CANCELADO'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, inicio);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pedidos"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
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
  public Flow<Integer> contarPorEstado(final String estado) {
    final String _sql = "SELECT COUNT(*) FROM pedidos WHERE estado = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, estado);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"pedidos"}, new Callable<Integer>() {
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

  private void __fetchRelationshipdetallePedidoAscomTecsupCafecampusDataLocalEntityDetallePedido(
      @NonNull final LongSparseArray<ArrayList<DetallePedido>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipdetallePedidoAscomTecsupCafecampusDataLocalEntityDetallePedido(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`pedidoId`,`productoId`,`nombreProducto`,`cantidad`,`precioUnitario` FROM `detalle_pedido` WHERE `pedidoId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "pedidoId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfPedidoId = 1;
      final int _cursorIndexOfProductoId = 2;
      final int _cursorIndexOfNombreProducto = 3;
      final int _cursorIndexOfCantidad = 4;
      final int _cursorIndexOfPrecioUnitario = 5;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<DetallePedido> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final DetallePedido _item_1;
          final int _tmpId;
          _tmpId = _cursor.getInt(_cursorIndexOfId);
          final int _tmpPedidoId;
          _tmpPedidoId = _cursor.getInt(_cursorIndexOfPedidoId);
          final int _tmpProductoId;
          _tmpProductoId = _cursor.getInt(_cursorIndexOfProductoId);
          final String _tmpNombreProducto;
          _tmpNombreProducto = _cursor.getString(_cursorIndexOfNombreProducto);
          final int _tmpCantidad;
          _tmpCantidad = _cursor.getInt(_cursorIndexOfCantidad);
          final double _tmpPrecioUnitario;
          _tmpPrecioUnitario = _cursor.getDouble(_cursorIndexOfPrecioUnitario);
          _item_1 = new DetallePedido(_tmpId,_tmpPedidoId,_tmpProductoId,_tmpNombreProducto,_tmpCantidad,_tmpPrecioUnitario);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
