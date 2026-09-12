# Sprint 1 – Review

**Fecha:** [completar]
**Asistentes:** Product Owner (Docente), Development Team

## Incremento demostrado
1. Landing page pública y responsiva (`index.jsp`) con buscador rápido y sección de propiedades destacadas.
2. Registro de usuarios (`registro.jsp` + `RegistroServlet`) con validaciones de formato y captura del error de correo duplicado.
3. Login (`login.jsp` + `LoginServlet`) con verificación de contraseña cifrada (BCrypt) contra la base de datos y redirección automática al panel según el rol.
4. Cierre de sesión (`LogoutServlet`) con invalidación de `HttpSession` y registro en auditoría.
5. `AuthFilter` protegiendo `/dashboard/*`: se demuestra el acceso denegado al intentar entrar por URL sin sesión o con el rol incorrecto.
6. Modelo de datos completo (MER + relacional 3FN) ejecutado en MySQL local, con datos de prueba cargados.

## Historias completadas vs. planeadas
- Completadas: 1, 2, 3 (100%).
- Historia 4: completada parcialmente (modelo N:M usuario_rol listo; interfaz de administración de roles se deja para Sprint 2, ya que depende del CRUD de usuarios).

## Feedback del Product Owner
[Espacio para registrar observaciones del docente durante la sustentación del Sprint].

## Métricas
- Puntos comprometidos: 21
- Puntos completados: 18 (parcial en historia 4)
- Commits realizados durante el sprint: [completar con historial real de Git]
