package com.tecsup.cafecampus.data.local

import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.model.Rol

object DatosIniciales {
    suspend fun sembrar(db: CafeDatabase) {
        if (db.usuarioDao().contar() == 0) {
            db.usuarioDao().insertar(
                Usuario(
                    nombre = "Juan León",
                    correo = "juan@campus.test",
                    clave = "1234",
                    rol = Rol.CLIENTE.name
                )
            )
            db.usuarioDao().insertar(
                Usuario(
                    nombre = "Administrador Cafetería",
                    correo = "admin@cafe.test",
                    clave = "admin",
                    rol = Rol.ADMIN.name
                )
            )
        }

        if (db.productoDao().contar() == 0) {
            val productosIniciales = listOf(
                Producto(
                    nombre = "Café americano",
                    descripcion = "Café pasado con granos seleccionados",
                    categoria = "Bebidas",
                    precio = 4.00,
                    disponible = true,
                    icono = "cafe"
                ),
                Producto(
                    nombre = "Sándwich de pollo",
                    descripcion = "Pollo deshilachado con mayonesa de la casa",
                    categoria = "Sandwiches",
                    precio = 9.00,
                    disponible = true,
                    icono = "plato"
                ),
                Producto(
                    nombre = "Menú del día",
                    descripcion = "Entrada, segundo y refresco del día",
                    categoria = "Platos",
                    precio = 12.00,
                    disponible = true,
                    icono = "plato"
                ),
                Producto(
                    nombre = "Jugo de naranja",
                    descripcion = "Jugo natural recién exprimido",
                    categoria = "Bebidas",
                    precio = 5.00,
                    disponible = true,
                    icono = "jugo"
                ),
                Producto(
                    nombre = "Empanada",
                    descripcion = "Empanada horneada de carne",
                    categoria = "Snacks",
                    precio = 4.50,
                    disponible = true,
                    icono = "pan"
                ),
                Producto(
                    nombre = "Chicha morada",
                    descripcion = "Chicha morada natural helada",
                    categoria = "Bebidas",
                    precio = 3.50,
                    disponible = true,
                    icono = "botella"
                )
            )
            for (p in productosIniciales) {
                db.productoDao().insertar(p)
            }
        }
    }
}
