# Microservicios Tienda

Sistema backend para la gestion de una tienda, construido con una arquitectura de microservicios usando Java, Spring Boot y Maven. El proyecto esta organizado como un proyecto Maven multi-modulo, donde cada servicio atiende una responsabilidad especifica del dominio.

La aplicacion separa las responsabilidades principales de una tienda: usuarios y clientes, productos y proveedores, ventas, compras y un API Gateway que centraliza el acceso HTTP hacia los servicios.

## Arquitectura general

El proyecto esta compuesto por los siguientes modulos:

| Modulo | Puerto | Responsabilidad |
| --- | ---: | --- |
| `api-gateway` | `8080` | Punto de entrada principal. Redirige las peticiones hacia los microservicios internos. |
| `usuario-service` | `8081` | Gestion de usuarios y clientes. |
| `producto-service` | `8082` | Gestion de categorias, productos y proveedores. |
| `ventas-service` | `8083` | Gestion de tickets de venta y detalle de tickets. |
| `compras-service` | `8084` | Gestion de compras y detalle de compras. |

## Tecnologias utilizadas

- Java 21
- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Spring Web
- Spring Data JPA
- Spring Cloud Gateway
- OpenFeign
- Spring Boot Actuator
- Bean Validation
- MySQL
- Lombok
- Maven

## Estructura del repositorio

```text
microservicios-tienda/
├── api-gateway/
├── usuario-service/
├── producto-service/
├── ventas-service/
├── compras-service/
├── pom.xml
└── README.md
```

## Descripcion de los servicios

### API Gateway

El modulo `api-gateway` funciona como entrada principal del sistema. Expone el puerto `8080` y enruta las solicitudes hacia los servicios correspondientes.

Rutas configuradas:

| Ruta | Servicio destino |
| --- | --- |
| `/api/usuarios/**` | `usuario-service` |
| `/api/clientes/**` | `usuario-service` |
| `/api/categorias/**` | `producto-service` |
| `/api/productos/**` | `producto-service` |
| `/api/proveedores/**` | `producto-service` |
| `/api/tickets/**` | `ventas-service` |
| `/api/detalle-ticket/**` | `ventas-service` |
| `/api/compras/**` | `compras-service` |
| `/api/detalle-compra/**` | `compras-service` |

Tambien incluye endpoints de fallback bajo `/fallback`.

### Usuario Service

Servicio encargado de la administracion de usuarios y clientes. Utiliza la base de datos `tienda_usuarios`.

Entidades principales:

- Usuario
- Cliente

Endpoints principales:

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| `GET` | `/api/usuarios` | Lista todos los usuarios. |
| `GET` | `/api/usuarios/{id}` | Obtiene un usuario por ID. |
| `GET` | `/api/usuarios/username/{username}` | Busca un usuario por nombre de usuario. |
| `POST` | `/api/usuarios` | Crea un usuario. |
| `PUT` | `/api/usuarios/{id}` | Actualiza un usuario. |
| `DELETE` | `/api/usuarios/{id}` | Elimina un usuario. |
| `GET` | `/api/clientes` | Lista todos los clientes. |
| `GET` | `/api/clientes/{id}` | Obtiene un cliente por ID. |
| `POST` | `/api/clientes` | Crea un cliente. |
| `PUT` | `/api/clientes/{id}` | Actualiza un cliente. |
| `DELETE` | `/api/clientes/{id}` | Elimina un cliente. |

### Producto Service

Servicio encargado de administrar categorias, productos y proveedores. Utiliza la base de datos `tienda_productos`.

Entidades principales:

- Categoria
- Producto
- Proveedor

Endpoints principales:

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| `GET` | `/api/categorias` | Lista todas las categorias. |
| `GET` | `/api/categorias/{id}` | Obtiene una categoria por ID. |
| `POST` | `/api/categorias` | Crea una categoria. |
| `PUT` | `/api/categorias/{id}` | Actualiza una categoria. |
| `DELETE` | `/api/categorias/{id}` | Elimina una categoria. |
| `GET` | `/api/productos` | Lista todos los productos. |
| `GET` | `/api/productos/{id}` | Obtiene un producto por ID. |
| `GET` | `/api/productos/codigo/{codigo}` | Busca un producto por codigo. |
| `GET` | `/api/productos/categoria/{categoriaId}` | Lista productos por categoria. |
| `POST` | `/api/productos` | Crea un producto. |
| `PUT` | `/api/productos/{id}` | Actualiza un producto. |
| `PATCH` | `/api/productos/{id}/stock?cantidad={cantidad}` | Actualiza el stock de un producto. |
| `DELETE` | `/api/productos/{id}` | Elimina un producto. |
| `GET` | `/api/proveedores` | Lista todos los proveedores. |
| `GET` | `/api/proveedores/{id}` | Obtiene un proveedor por ID. |
| `POST` | `/api/proveedores` | Crea un proveedor. |
| `PUT` | `/api/proveedores/{id}` | Actualiza un proveedor. |
| `DELETE` | `/api/proveedores/{id}` | Elimina un proveedor. |

### Ventas Service

Servicio encargado de registrar tickets de venta. Utiliza la base de datos `tienda_ventas`.

Este servicio se comunica con otros servicios mediante OpenFeign para consultar informacion de clientes y productos.

Clientes Feign:

- `ClienteFeignClient`: consulta clientes en `/api/clientes/{id}`.
- `ProductoFeignClient`: consulta productos en `/api/productos/{id}`.

Endpoints principales:

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| `GET` | `/api/tickets` | Lista todos los tickets. |
| `GET` | `/api/tickets/{id}` | Obtiene un ticket por ID. |
| `POST` | `/api/tickets` | Crea un ticket de venta. |
| `PATCH` | `/api/tickets/{id}/anular` | Anula un ticket. |
| `GET` | `/api/detalle-ticket/ticket/{ticketId}` | Lista los detalles de un ticket. |

### Compras Service

Servicio encargado de registrar compras a proveedores. Utiliza la base de datos `tienda_compras`.

Este servicio se comunica con otros servicios mediante OpenFeign para consultar informacion de proveedores y productos.

Clientes Feign:

- `ProveedorFeignClient`: consulta proveedores en `/api/proveedores/{id}`.
- `ProductoFeignClient`: consulta productos en `/api/productos/{id}`.

Endpoints principales:

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| `GET` | `/api/compras` | Lista todas las compras. |
| `GET` | `/api/compras/{id}` | Obtiene una compra por ID. |
| `POST` | `/api/compras` | Crea una compra. |
| `PATCH` | `/api/compras/{id}/anular` | Anula una compra. |
| `GET` | `/api/detalle-compra/compra/{compraId}` | Lista los detalles de una compra. |

## Bases de datos

Cada microservicio utiliza su propia base de datos MySQL:

| Servicio | Base de datos |
| --- | --- |
| `usuario-service` | `tienda_usuarios` |
| `producto-service` | `tienda_productos` |
| `ventas-service` | `tienda_ventas` |
| `compras-service` | `tienda_compras` |

Configuracion actual usada por los servicios:

```yaml
username: root
password: 2403
driver-class-name: com.mysql.cj.jdbc.Driver
```

Antes de ejecutar los servicios, crea las bases de datos:

```sql
CREATE DATABASE tienda_usuarios;
CREATE DATABASE tienda_productos;
CREATE DATABASE tienda_ventas;
CREATE DATABASE tienda_compras;
```

La configuracion JPA usa `ddl-auto: update`, por lo que Hibernate puede crear o actualizar las tablas automaticamente al iniciar cada servicio.

## Requisitos previos

- JDK 21 instalado.
- Maven instalado.
- MySQL en ejecucion.
- Bases de datos creadas.
- Puertos `8080`, `8081`, `8082`, `8083` y `8084` disponibles.

## Compilacion del proyecto

Desde la raiz del repositorio:

```bash
mvn clean install
```

Tambien puedes compilar un modulo especifico:

```bash
mvn clean install -pl usuario-service
```

## Ejecucion de los servicios

Se recomienda iniciar primero los servicios internos y despues el API Gateway.

### Ejecutar usuario-service

```bash
cd usuario-service
mvn spring-boot:run
```

Servicio disponible en:

```text
http://localhost:8081
```

### Ejecutar producto-service

```bash
cd producto-service
mvn spring-boot:run
```

Servicio disponible en:

```text
http://localhost:8082
```

### Ejecutar ventas-service

```bash
cd ventas-service
mvn spring-boot:run
```

Servicio disponible en:

```text
http://localhost:8083
```

### Ejecutar compras-service

```bash
cd compras-service
mvn spring-boot:run
```

Servicio disponible en:

```text
http://localhost:8084
```

### Ejecutar api-gateway

```bash
cd api-gateway
mvn spring-boot:run
```

Gateway disponible en:

```text
http://localhost:8080
```

## Uso mediante API Gateway

Una vez que todos los servicios esten levantados, puedes consumir la API desde el Gateway usando el puerto `8080`.

Ejemplos:

```bash
curl http://localhost:8080/api/usuarios
curl http://localhost:8080/api/clientes
curl http://localhost:8080/api/categorias
curl http://localhost:8080/api/productos
curl http://localhost:8080/api/proveedores
curl http://localhost:8080/api/tickets
curl http://localhost:8080/api/compras
```

## Ejemplos de peticiones

### Crear un usuario

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123",
    "email": "admin@tienda.com",
    "rol": "ADMIN"
  }'
```

### Crear un cliente

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellido": "Perez",
    "telefono": "5551234567",
    "email": "juan@correo.com",
    "direccion": "Av. Principal 123"
  }'
```

### Crear una categoria

```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Electronica",
    "descripcion": "Productos electronicos"
  }'
```

### Crear un producto

```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Mouse inalambrico",
    "codigo": "MOU-001",
    "descripcion": "Mouse ergonomico",
    "precio": 250.00,
    "stock": 50,
    "categoriaId": 1
  }'
```

### Crear un ticket de venta

```bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "usuarioId": 1,
    "detalles": [
      {
        "productoId": 1,
        "cantidad": 2
      }
    ]
  }'
```

### Crear una compra

```bash
curl -X POST http://localhost:8080/api/compras \
  -H "Content-Type: application/json" \
  -d '{
    "proveedorId": 1,
    "detalles": [
      {
        "productoId": 1,
        "cantidad": 10,
        "precioUnitario": 180.00
      }
    ]
  }'
```

## Actuator

Los servicios exponen endpoints de monitoreo con Spring Boot Actuator.

Ejemplos:

```text
http://localhost:8080/actuator/health
http://localhost:8081/actuator/health
http://localhost:8082/actuator/health
http://localhost:8083/actuator/health
http://localhost:8084/actuator/health
```

## Notas importantes

- El proyecto usa Maven multi-modulo desde el `pom.xml` raiz.
- Cada microservicio tiene su propio `application.yml`.
- Los servicios de ventas y compras dependen de consultas a otros servicios mediante OpenFeign.
- El API Gateway enruta por path hacia servicios locales.
- La configuracion actual esta pensada para ejecucion local.
- Si cambias credenciales de MySQL, tambien debes actualizar los archivos `application.yml` correspondientes.

## Repositorio

https://github.com/2003lulu/microservicios-tienda.git
