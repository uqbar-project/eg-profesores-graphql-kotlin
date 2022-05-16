# Ejemplo Profesores y Materias GraphQL

[![build](https://github.com/uqbar-project/eg-profesores-graphql-kotlin/actions/workflows/build.yml/badge.svg)](https://github.com/uqbar-project/eg-profesores-graphql-kotlin/actions/workflows/build.yml)
[![codecov](https://codecov.io/gh/uqbar-project/eg-profesores-graphql-kotlin/branch/master/graph/badge.svg?token=WQCwF1TJKR)](https://codecov.io/gh/uqbar-project/eg-profesores-graphql-kotlin)

## GraphQL

En la [variante REST](https://github.com/uqbar-project/eg-profesores-springboot-kotlin) hemos implementado varios endpoints para una aplicación que relaciona materias y sus correspondientes profesores (una relación many-to-many). En esta versión estaremos resolviendo requerimientos similares utilizando la especificación **GraphQL**.

## Para más información

Pueden ver [esta presentación](https://docs.google.com/presentation/d/187zouUsasCy-SYEQybZBYONOh5BDKrLHdQib3--zn9E/edit#slide=id.p).

## GraphiQL para testeo local

- Levantamos la aplicación y luego en un navegador consultamos

```bash
http://localhost:8080/graphiql
```

Podemos ejecutar consultas custom:

```graphql
{
  profesores {
    nombre
    apellido
    puntajeDocente
    materias {
      nombre
      anio
    }
  }
}
```

E incluso podemos agregar `sitioWeb` a nuestro query, y navegar la estructura del profesor:

![graphiql](./videos/graphiql.gif)

### Scalar

Si nos fijamos en la definición del objeto de dominio Materia

```kt
@Entity
class Materia(
   @Column var nombre: String = "",
   @Column var anio: Int,
   @Column var codigo: String,
   @Column var sitioWeb: URL,
   @Column var cargaHoraSemanal: Int
) {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   var id: Long = 0
}
```

la URL es de tipo `java.net.URL`. ¿Cómo es que se convierte eso en un string adecuado?

Definimos URL como un **scalar**. GraphQL necesita replicar el modelo en su propio esquema, que podés ver en el archivo [schema.graphqls](./src/main/resources/schema/schema.graphqls)

```graphql
scalar URL

...

type Materia {
    nombre: String
    anio: Int
    codigo: String
    sitioWeb: URL
    cargaHoraSemanal: Int
}
```

GraphQL provee los tipos de dato estándar: String, Int (no LocalDate por ejemplo). Para la URL es necesario primero definirlo como un tipo de dato **scalar**. Eso requiere que implementemos una clase asociada a este scalar para hacer las conversiones desde y hacia los endpoints:

```kt
@DgsScalar(name = "URL")
class URLScalar : Coercing<URL, String> {
   // Convierto de URL a String para serializar la información cuando se devuelve un query
   override fun serialize(dataFetcherResult: Any): String {
      return (dataFetcherResult as? URL)?.toString()
         ?: throw CoercingSerializeException("El objeto no es de tipo URL: ${dataFetcherResult.javaClass.name}")
   }

   // Convierto de String a URL para deserializar la información en las mutaciones o queries que aceptan parámetros
   override fun parseValue(input: Any): URL {
      return try {
         if (input is String) {
            URL(input)
         } else {
            throw CoercingParseValueException("[GraphQL - URL] - El valor no es un string: [${input}]")
         }
      } catch (e: MalformedURLException) {
         throw CoercingParseValueException(
            "[GraphQL - URL] - URL inválida: [${input}]", e
         )
      }
   }

   override fun parseLiteral(input: Any): URL {
      return if (input is StringValue) {
         try {
            URL(input.value)
         } catch (e: MalformedURLException) {
            throw CoercingParseLiteralException(e)
         }
      } else {
         throw CoercingParseLiteralException("[GraphQL - URL] - El valor no es un string: [${input}]")
      }
   }
}
```

### Query

Por otra parte, podemos ver que ese archivo define un tipo Query para poder hacer consultas:

```graphql
...

type Query {
    profesores(nombreFilter: String): [Profesor]
}

type Profesor {
    nombre: String
    apellido: String
    anioComienzo: Int
    puntajeDocente: Int
    materias: [Materia]
}

type Materia { ... }
```

Eso hace que graphiql permita navegar el esquema en la parte derecha del navegador. El query se implementa delegando al repository, y reemplaza en esta arquitectura al par controller/service:

```kt
@DgsComponent
class ProfesoresDataFetcher {

   @Autowired
   lateinit var profesorRepository: ProfesorRepository

   @DgsQuery
   fun profesores(@InputArgument nombreFilter : String?) =
      profesorRepository.findAllByNombreCompleto((nombreFilter ?: "") + "%")

}
```

### Filtrando por nombre

El parámetro que define el schema:

```graphql
type Query {
    profesores(nombreFilter: String): [Profesor]
}
```

es recibido por el fetcher que a su vez delega la consulta al repository:

```kotlin
@DgsQuery
fun profesores(@InputArgument nombreFilter : String?) =
  profesorRepository.findAllByNombreCompleto((nombreFilter ?: "") + "%")
```

Eso nos permite consultar pasando como valor el nombre o apellido de una persona docente:

```graphql
{
    profesores(nombreFilter: "Lu") {
        nombre
        apellido
        puntajeDocente
        materias {
            nombre
            anio
        }
    }
}
```

### Agregando un nuevo query

Podemos también hacer una consulta de un profesor específico...

### Mutations

TODO

### Testing

TODO: Explicar

