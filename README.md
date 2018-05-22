> ### Nota: La app ya no está en desarrollo y algunas funciones ya no tiran (por ejemplo, el login) sin embargo si estás desarrollando alguna app para ForoCoches o tienes curiosidad por saber cómo hacer funcionar algo así estoy seguro de que la mayoría de cosas aún funcionarán. 

# OpenCoches (A.K.A. una aplicación para ForoCoches)
[![Gitter](https://img.shields.io/gitter/room/nwjs/nw.js.svg?maxAge=2592000)](https://gitter.im/OpenCoches/Lobby)

:red_car: Una aplicación de Forocoches para Android, al menos hasta que el jefe nos la cape.

Para el funcionamiento de la misma, dado que ilitri no quiere darnos herramientas para acceder al contenido del foro de forma digna, imito el acceso desde un navegador y parseo los datos para que sean más fáciles de leer dentro de la aplicación.

**NOTA**: En **ningún** momento se recopilan los datos del usuario. Como se puede ver en el código los datos de la cuenta que se introducen en la aplicación no se usan para nada más que loguearnos en la web.

## Lenguaje, frameworks y blablabla
[![forthebadge](http://forthebadge.com/images/badges/fuck-it-ship-it.svg)](http://forthebadge.com)
[![forthebadge](http://forthebadge.com/images/badges/powered-by-electricity.svg)](http://forthebadge.com)
[![forthebadge](http://forthebadge.com/images/badges/gluten-free.svg)](http://forthebadge.com)

Toda la aplicación está hecha con *Kotlin* y usando el SDK de Android. Para todo lo referente a las peticiones y parseo del HTML he usado *JSoup*. Además, para facilitar el tema *async* he optado por el uso de *RxJava* y *RxAndroid*.

## Para compilar...
Dado que el proyecto está integrado con *Firebase* para el reporte de errores (cosa que, creedme, será muy muy útil) necesitáis una cuenta y un proyecto en dicha página para poder compilar *OpenCoches*. 

Básicamente sólo necesitáis el archivo de configuración *google-services.json* el cual conseguís siguiendo el tutorial básico que tienen en la página de *Firebase* en la raíz de la aplicación (el directorio *app*).

**Alternativamente** podéis comentar lo referente a *Firebase* en el proyecto, es decir: los imports en los archivos *Gradle* tanto del proyecto como el del app y lo anotado en la clase FirebaseReporter.