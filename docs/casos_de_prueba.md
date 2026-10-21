# Casos de prueba – Sistema de Gestión de Productos

**Instrucciones:** ejecute cada caso en orden con la aplicación conectada a MySQL. Escriba en la columna *Resultado obtenido* lo que realmente ocurrió y cambie el *Estado* a **Aprobado** o **Fallido**. Mientras no se ejecute, el estado es **Pendiente**.

Datos base usados en las pruebas: se parte de la tabla `productos` vacía (o sin los códigos P001, P002 y P999).

| ID | Caso | Datos de entrada | Resultado esperado | Resultado obtenido | Estado |
|----|------|------------------|--------------------|--------------------|--------|
| CP01 | Registro correcto | Código: P001 · Nombre: Arroz 500 g · Descripción: Bolsa · Precio: 2500 · Cantidad: 40 → **Registrar** | Mensaje "Producto registrado correctamente." Los campos se limpian y P001 aparece en la tabla. | [Por completar] | Pendiente |
| CP02 | Registro con campos vacíos | Código vacío; Nombre: Leche; Precio: 3000; Cantidad: 10 → **Registrar**. Repetir con Nombre vacío, y con Precio vacío. | Mensajes "El código no puede estar vacío.", "El nombre no puede estar vacío." y "El precio no puede estar vacío." No se registra nada. | [Por completar] | Pendiente |
| CP03 | Precio negativo | Código: P003 · Nombre: Leche · Precio: -100 · Cantidad: 10 → **Registrar** | Mensaje "El precio no puede ser negativo." No se registra. | [Por completar] | Pendiente |
| CP04 | Cantidad negativa | Código: P003 · Nombre: Leche · Precio: 3000 · Cantidad: -5 → **Registrar** | Mensaje "La cantidad no puede ser negativa." No se registra. | [Por completar] | Pendiente |
| CP05 | Cantidad con texto | Código: P003 · Nombre: Leche · Precio: 3000 · Cantidad: diez → **Registrar** | Mensaje "La cantidad debe ser un número entero (sin letras ni decimales)." No se registra. | [Por completar] | Pendiente |
| CP06 | Búsqueda por código | Registrar antes P002 (Aceite, 8900.50, 15). Código: P002 → **Buscar** | La tabla muestra solo P002 y sus datos se cargan en los campos. | [Por completar] | Pendiente |
| CP07 | Búsqueda por nombre | Código vacío · Nombre: arroz → **Buscar** | La tabla muestra los productos cuyo nombre contiene "arroz" y un mensaje con la cantidad encontrada. | [Por completar] | Pendiente |
| CP08 | Actualización correcta | Pulsar **Consultar / Actualizar tabla** y seleccionar P001 en la tabla; cambiar Descripción: Bolsa 500 g · Precio: 2700 · Cantidad: 35 → **Actualizar** | Mensaje "Producto actualizado correctamente." La tabla muestra los nuevos valores de P001. | [Por completar] | Pendiente |
| CP09 | Actualización de producto inexistente | Código: P999 · Precio: 100 · Cantidad: 1 → **Actualizar** | Mensaje "No existe un producto con el código "P999". No se actualizó nada." | [Por completar] | Pendiente |
| CP10 | Eliminación correcta | Código: P002 → **Eliminar** → responder **Sí** | Aparece la confirmación con el nombre del producto; luego "Producto eliminado correctamente." P002 desaparece de la tabla. | [Por completar] | Pendiente |
| CP11 | Cancelación de eliminación | Código: P001 → **Eliminar** → responder **No** | Mensaje "Eliminación cancelada. El producto no fue eliminado." P001 sigue en la tabla. | [Por completar] | Pendiente |
| CP12 | Persistencia | Con P001 registrado: cerrar la aplicación, volver a ejecutarla. | Al abrir, la tabla carga automáticamente P001 con los datos actualizados en CP08. También aparece con `SELECT * FROM productos;` en MySQL. | [Por completar] | Pendiente |
| CP13 | Error de conexión | Detener el servicio de MySQL y abrir la aplicación (o pulsar **Consultar / Actualizar tabla**). | Mensaje "No se pudo conectar con MySQL. Verifique que el servidor MySQL esté encendido…". La aplicación no se cierra. | [Por completar] | Pendiente |

### Casos adicionales recomendados (opcionales)

| ID | Caso | Datos de entrada | Resultado esperado | Resultado obtenido | Estado |
|----|------|------------------|--------------------|--------------------|--------|
| CP14 | Código duplicado | Registrar otra vez Código: P001 con cualquier nombre, precio y cantidad válidos | Mensaje "Ya existe un producto con el código "P001"." | [Por completar] | Pendiente |
| CP15 | Precio con texto | Precio: abc | Mensaje "El precio debe ser un valor numérico (ejemplo: 2500 o 2500.50)." | [Por completar] | Pendiente |
| CP16 | Credenciales incorrectas | Cambiar `CONTRASENA` en ConexionBD.java por una incorrecta y ejecutar | Mensaje "Acceso denegado a MySQL. Revise el usuario y la contraseña…" | [Por completar] | Pendiente |
