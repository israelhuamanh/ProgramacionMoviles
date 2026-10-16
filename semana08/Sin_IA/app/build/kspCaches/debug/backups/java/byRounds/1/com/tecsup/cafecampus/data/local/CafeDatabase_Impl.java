package com.tecsup.cafecampus.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.tecsup.cafecampus.data.local.dao.PedidoDao;
import com.tecsup.cafecampus.data.local.dao.PedidoDao_Impl;
import com.tecsup.cafecampus.data.local.dao.ProductoDao;
import com.tecsup.cafecampus.data.local.dao.ProductoDao_Impl;
import com.tecsup.cafecampus.data.local.dao.UsuarioDao;
import com.tecsup.cafecampus.data.local.dao.UsuarioDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CafeDatabase_Impl extends CafeDatabase {
  private volatile ProductoDao _productoDao;

  private volatile UsuarioDao _usuarioDao;

  private volatile PedidoDao _pedidoDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `productos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `categoria` TEXT NOT NULL, `precio` REAL NOT NULL, `disponible` INTEGER NOT NULL, `icono` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `usuarios` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nombre` TEXT NOT NULL, `correo` TEXT NOT NULL, `clave` TEXT NOT NULL, `rol` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pedidos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clienteId` INTEGER NOT NULL, `estado` TEXT NOT NULL, `horaRecojo` TEXT NOT NULL, `pago` TEXT NOT NULL, `nota` TEXT NOT NULL, `total` REAL NOT NULL, `fecha` INTEGER NOT NULL, FOREIGN KEY(`clienteId`) REFERENCES `usuarios`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_pedidos_clienteId` ON `pedidos` (`clienteId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `detalle_pedido` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pedidoId` INTEGER NOT NULL, `productoId` INTEGER NOT NULL, `nombreProducto` TEXT NOT NULL, `cantidad` INTEGER NOT NULL, `precioUnitario` REAL NOT NULL, FOREIGN KEY(`pedidoId`) REFERENCES `pedidos`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_detalle_pedido_pedidoId` ON `detalle_pedido` (`pedidoId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd3f8dc7f3d183c718c24dcf9d6b4c3c7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `productos`");
        db.execSQL("DROP TABLE IF EXISTS `usuarios`");
        db.execSQL("DROP TABLE IF EXISTS `pedidos`");
        db.execSQL("DROP TABLE IF EXISTS `detalle_pedido`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsProductos = new HashMap<String, TableInfo.Column>(7);
        _columnsProductos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("descripcion", new TableInfo.Column("descripcion", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("categoria", new TableInfo.Column("categoria", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("precio", new TableInfo.Column("precio", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("disponible", new TableInfo.Column("disponible", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProductos.put("icono", new TableInfo.Column("icono", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProductos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProductos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProductos = new TableInfo("productos", _columnsProductos, _foreignKeysProductos, _indicesProductos);
        final TableInfo _existingProductos = TableInfo.read(db, "productos");
        if (!_infoProductos.equals(_existingProductos)) {
          return new RoomOpenHelper.ValidationResult(false, "productos(com.tecsup.cafecampus.data.local.entity.Producto).\n"
                  + " Expected:\n" + _infoProductos + "\n"
                  + " Found:\n" + _existingProductos);
        }
        final HashMap<String, TableInfo.Column> _columnsUsuarios = new HashMap<String, TableInfo.Column>(5);
        _columnsUsuarios.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("nombre", new TableInfo.Column("nombre", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("correo", new TableInfo.Column("correo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("clave", new TableInfo.Column("clave", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("rol", new TableInfo.Column("rol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsuarios = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsuarios = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsuarios = new TableInfo("usuarios", _columnsUsuarios, _foreignKeysUsuarios, _indicesUsuarios);
        final TableInfo _existingUsuarios = TableInfo.read(db, "usuarios");
        if (!_infoUsuarios.equals(_existingUsuarios)) {
          return new RoomOpenHelper.ValidationResult(false, "usuarios(com.tecsup.cafecampus.data.local.entity.Usuario).\n"
                  + " Expected:\n" + _infoUsuarios + "\n"
                  + " Found:\n" + _existingUsuarios);
        }
        final HashMap<String, TableInfo.Column> _columnsPedidos = new HashMap<String, TableInfo.Column>(8);
        _columnsPedidos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("clienteId", new TableInfo.Column("clienteId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("estado", new TableInfo.Column("estado", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("horaRecojo", new TableInfo.Column("horaRecojo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("pago", new TableInfo.Column("pago", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("nota", new TableInfo.Column("nota", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("total", new TableInfo.Column("total", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPedidos.put("fecha", new TableInfo.Column("fecha", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPedidos = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPedidos.add(new TableInfo.ForeignKey("usuarios", "NO ACTION", "NO ACTION", Arrays.asList("clienteId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPedidos = new HashSet<TableInfo.Index>(1);
        _indicesPedidos.add(new TableInfo.Index("index_pedidos_clienteId", false, Arrays.asList("clienteId"), Arrays.asList("ASC")));
        final TableInfo _infoPedidos = new TableInfo("pedidos", _columnsPedidos, _foreignKeysPedidos, _indicesPedidos);
        final TableInfo _existingPedidos = TableInfo.read(db, "pedidos");
        if (!_infoPedidos.equals(_existingPedidos)) {
          return new RoomOpenHelper.ValidationResult(false, "pedidos(com.tecsup.cafecampus.data.local.entity.Pedido).\n"
                  + " Expected:\n" + _infoPedidos + "\n"
                  + " Found:\n" + _existingPedidos);
        }
        final HashMap<String, TableInfo.Column> _columnsDetallePedido = new HashMap<String, TableInfo.Column>(6);
        _columnsDetallePedido.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedido.put("pedidoId", new TableInfo.Column("pedidoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedido.put("productoId", new TableInfo.Column("productoId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedido.put("nombreProducto", new TableInfo.Column("nombreProducto", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedido.put("cantidad", new TableInfo.Column("cantidad", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDetallePedido.put("precioUnitario", new TableInfo.Column("precioUnitario", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDetallePedido = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysDetallePedido.add(new TableInfo.ForeignKey("pedidos", "CASCADE", "NO ACTION", Arrays.asList("pedidoId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDetallePedido = new HashSet<TableInfo.Index>(1);
        _indicesDetallePedido.add(new TableInfo.Index("index_detalle_pedido_pedidoId", false, Arrays.asList("pedidoId"), Arrays.asList("ASC")));
        final TableInfo _infoDetallePedido = new TableInfo("detalle_pedido", _columnsDetallePedido, _foreignKeysDetallePedido, _indicesDetallePedido);
        final TableInfo _existingDetallePedido = TableInfo.read(db, "detalle_pedido");
        if (!_infoDetallePedido.equals(_existingDetallePedido)) {
          return new RoomOpenHelper.ValidationResult(false, "detalle_pedido(com.tecsup.cafecampus.data.local.entity.DetallePedido).\n"
                  + " Expected:\n" + _infoDetallePedido + "\n"
                  + " Found:\n" + _existingDetallePedido);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "d3f8dc7f3d183c718c24dcf9d6b4c3c7", "dc01b4a925d8aa59e2646e9525ed342e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "productos","usuarios","pedidos","detalle_pedido");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `productos`");
      _db.execSQL("DELETE FROM `pedidos`");
      _db.execSQL("DELETE FROM `usuarios`");
      _db.execSQL("DELETE FROM `detalle_pedido`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ProductoDao.class, ProductoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UsuarioDao.class, UsuarioDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PedidoDao.class, PedidoDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ProductoDao productoDao() {
    if (_productoDao != null) {
      return _productoDao;
    } else {
      synchronized(this) {
        if(_productoDao == null) {
          _productoDao = new ProductoDao_Impl(this);
        }
        return _productoDao;
      }
    }
  }

  @Override
  public UsuarioDao usuarioDao() {
    if (_usuarioDao != null) {
      return _usuarioDao;
    } else {
      synchronized(this) {
        if(_usuarioDao == null) {
          _usuarioDao = new UsuarioDao_Impl(this);
        }
        return _usuarioDao;
      }
    }
  }

  @Override
  public PedidoDao pedidoDao() {
    if (_pedidoDao != null) {
      return _pedidoDao;
    } else {
      synchronized(this) {
        if(_pedidoDao == null) {
          _pedidoDao = new PedidoDao_Impl(this);
        }
        return _pedidoDao;
      }
    }
  }
}
