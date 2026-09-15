# Sprint 1 – Review

## Evidencia
1. Landing page pública y responsiva (`index.jsp`) con buscador rápido y sección de propiedades destacadas.
2. Registro de usuarios (`registro.jsp` + `RegistroServlet`) con validaciones de formato y captura del error de correo duplicado.
3. Login (`login.jsp` + `LoginServlet`) con verificación de contraseña cifrada (BCrypt) contra la base de datos y redirección automática al panel según el rol.
4. Cierre de sesión (`LogoutServlet`) con invalidación de `HttpSession` y registro en auditoría.
5. `AuthFilter` protegiendo `/dashboard/*`: se demuestra el acceso denegado al intentar entrar por URL sin sesión o con el rol incorrecto.
6. Modelo de datos completo (MER + relacional 3FN) ejecutado en MySQL local, con datos de prueba cargados.

**Demo:** el login redirige solo al panel que corresponde según el rol, y si alguien intenta entrar a una ruta privada sin sesión, lo manda a acceso denegado.
