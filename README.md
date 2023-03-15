# tempo-challenge

## API First
La api se encuentra en la carpeta `src/doc` es un yaml compatible con swagger, open api version 3.1.0

### Endpoint Calcular Suma 
Se debe hacer un GET a `/calc/plus` pasando como query param _number1_ y _number2_. Admite hasta 3 request por minuto

### Endpoint History
Es simplemente un get a `/history`, no recibe ningun parametro.

## Configuración de la aplicacion
Se pueden configurar los siguiente parametros de la aplicación en el archivo application.yaml
* app.cache.ttl: Tiempo que dura la cache del servicio externo en segundos, por defecto 1800s, media hora.
* app.quote: Cantidad de request por minuto que se aceptan para el endpoint `calc/plus`. Por defecto 3

## Consideraciones

### Servicio externo
El servicio externo está moqueado en la clase `ExternalServiceSimpleMock`, se inyecta como servicio `ExternalService`.
#### Cache
Dado que el puede considerarse que el valor devuelvo por el servicio externo no cambia durante 30 minuros, se opró por cachear la llamada a este.

Para la cache se ha optado el metodo por annotations de spring y como cache se desidió utilizar Caffeine. De esta manera el matodo cacheado se implementa simplemente agregando una annotation.

`@Cacheable
public BigDecimal getFee() { return BigDecimal.TEN; } }`

#### Resilience
En el caso de que el servicio externo falle por algun motivo, se devuelve el ultimo valor obtenido, este valor se guarda en la memoria de la instancia.

## Historial de llamados
Como se especificó que se debe guardar el historial de request de todos los endpoints junto con su resultado (evitando el propio get /history, por razones obvias), se decidio optar por un Interceptor para todos los endpoints (menos el de history), el cual es implementado como un `ResponseBodyAdvice` dado que se debe obtener el body de la respuesta.
La clase es `EndpointInterceptor` el cual delega el comportamiento a `EndpointInterceptorExecutor` que es quien se encarga de ejecutar el guardado en forma asincrona (para no impacte en el tiempo de respuesta), y tampoco interfiere si se produce una falla. 

## Servicio Distribuido
Dado que el servicio debe comportarse como un servicio distribuido, he tomado en cuanta las siguientes opciones para asegurar los 3 RPM.

1. Usar un API gateway para
2. Usar un servicio de colas (kafka o rabit mq) y restrigir el tamaño a 3.
3. Usar Redis como memoria externa y usar lo operadores _increment_ y _decrement_ (que son operadores atomicos) como semasforo.

He optado por usar redis ya que usar un API gateway deja sin sentido la consigna, e usar redis me resultó mas simple que implementar un sistema de colas.

## Unit Tests
Siguiendo con mi estilo de programación, se implementaron los test unitarios utilizando TDD