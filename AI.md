# AI Context Document

## Contexto de la última sesión

Se han resuelto múltiples problemas relacionados con la gestión y actualización del **avatar de usuario** y el formulario de perfil. 

Los cambios realizados se dividen entre el Backend (`CartAI`) y el Frontend (`CartAI-WEB`).

### Problemas Solucionados

1. **Inconsistencia de campos en respuesta de subida:**
   - **Problema:** El backend devolvía `avatarFileId` usando un `id` vacío del adaptador S3/MinIO, mientras que el frontend esperaba `avatarFileURL`.
   - **Solución:** Se corrigió `UserAvatarRestController` para devolver el nombre de archivo de MinIO (`fileName`) en lugar de `id` vacío, y se actualizó el frontend (`identityService.ts` y `ProfilePage.tsx`) para usar el campo correcto (`avatarFileId`).

2. **Ruta de visualización de ficheros (Frontend):**
   - **Problema:** El frontend intentaba renderizar el avatar desde `/api/files/{id}`, un endpoint que no existe.
   - **Solución:** Se actualizó a `/api/storage/files/{id}` en `ProfilePage.tsx` y `Navbar.tsx` para que coincida con `StorageRestController`.

3. **Pérdida de identidad tras Login (Backend/Frontend):**
   - **Problema:** Tras el login, el backend no devolvía el `userId` ni el `avatarFileId` en `AuthRestResponse`, por lo que el store del frontend tenía esos campos como `undefined`. Esto provocaba que `PUT /api/users/{id}` fallase al enviarse como `undefined`.
   - **Solución:** Se añadieron `userId` y `avatarFileId` al record `AuthRestResponse` y se mapearon correctamente en `AuthRestMapper`.

4. **Bloqueo de CORS por Spring Security en preflight (Backend):**
   - **Problema:** Spring Security interceptaba y bloqueaba con HTTP 401 las peticiones preflight (`OPTIONS`) del formulario de actualización de perfil (`PUT`) antes de llegar a la configuración de CORS, causando un falso error CORS en el navegador.
   - **Solución:** Se añadió `OPTIONS` a los métodos permitidos en `application.properties` y se refactorizó `CorsConfig` para exponer un bean `CorsConfigurationSource`. Además, se añadió `.cors(Customizer.withDefaults())` en `SecurityConfig` para que Spring Security lo valide de forma nativa antes de la autenticación.

5. **Error de Deserialización de Roles en Jackson (Backend):**
   - **Problema:** El frontend enviaba un array de strings (ej. `["ADMIN"]`) pero `UpdateUserRestRequest` requería un `Set<Role>` completo, lo cual provocaba un `HttpMessageNotReadableException`.
   - **Solución:** Se modificó el DTO `UpdateUserRestRequest` para recibir `Set<String>` y se actualizó `UserRestController` para buscar los objetos `Role` en BD antes de pasarlos al caso de uso (igual que en la creación).

6. **Promoción de ficheros de bucket temporal a permanente (Backend):**
   - **Problema:** Al actualizar el perfil con un nuevo avatar, el caso de uso `UpdateUserUseCase` no movía el fichero de MinIO del bucket temporal (que auto-expira) al permanente.
   - **Solución:** Se implementó la lógica de `storagePort.promoteFile()` y limpieza de avatar antiguo dentro de `UpdateUserUseCase`.
