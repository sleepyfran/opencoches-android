# OpenCoches (A.K.A. una aplicación para ForoCoches)
[![Gitter](https://img.shields.io/gitter/room/nwjs/nw.js.svg?maxAge=2592000)](https://gitter.im/OpenCoches/Lobby)

:red_car: Una aplicación de Forocoches para Android, al menos hasta que el jefe nos la cape.

Para el funcionamiento de la misma, dado que ilitri no quiere darnos herramientas para acceder al contenido del foro de forma digna, imito el acceso desde un navegador y parseo los datos para que sean más fáciles de leer dentro de la aplicación.

## A dónde va el proyecto
Básicamente este proyecto lo he hecho en mi tiempo libre como forma de entretenimiento además de como proyecto personal para poder acceder al foro de una forma más cómoda.

Dicho esto no pienso dar soporte 24/7 ni arreglar los errores a la velocidad de la luz. La mayoría del código está escrito a altas horas de la noche (como ahora mismo que son las 4 de la mañana, *jé*) y, aunque he intentado hacerlo todo lo claro y modular posible, es probable que haya alguna que otra chusta por ahí.

## [Developers developers developers developers developers](https://www.youtube.com/watch?v=Vhh_GeBPOhs)
Si eres desarrollador y quieres colaborar: adelante, esta es tu casa. El código es completamente libre bajo la licencia de **GPL v3**. Si quieres contribuir sólo tienes que hacer un *pull request* y estaré encantado de meter en el proyecto todo lo que se merezca estar dentro. Dicho esto, en la medida de lo posible, intenta comentar lo que hagas y seguir el estilo general de código que sigo a lo largo de todo el proyecto. En la siguiente sección os hago una recopilación rapidita de las librerías y estilos que he usado a lo largo del proyecto, aunque tengo pendiente hacer una Wiki aquí en GitHub más detallada para dejarlo todo mejor comentado.

## Lenguaje, frameworks y blablabla
[![forthebadge](http://forthebadge.com/images/badges/fuck-it-ship-it.svg)](http://forthebadge.com)
[![forthebadge](http://forthebadge.com/images/badges/powered-by-electricity.svg)](http://forthebadge.com)
[![forthebadge](http://forthebadge.com/images/badges/gluten-free.svg)](http://forthebadge.com)

Toda la aplicación está hecha con *Kotlin* y usando el SDK de Android. Para la organización general del código he usado un pseudo-MVP (es mi primerito día usando MVP y quizá no sea todo al 100% como debería) y Dagger 2 para la inyección de dependencias.

Para todo lo referente a las peticiones y parseo del HTML he usado *JSoup*. Para todo el tema *async* he optado por el uso de *RxJava* y *RxAndroid*.

## Para compilar...
Dado que el proyecto está integrado con *Firebase* para el reporte de errores (cosa que, creedme, será muy muy útil) necesitáis una cuenta y un proyecto en dicha página para poder compilar *OpenCoches*. 

Básicamente sólo necesitáis el archivo de configuración *google-services.json* el cual conseguís siguiendo el tutorial básico que tienen en la página de *Firebase* en la raíz de la aplicación (el directorio *app*).

Obviamente otra alternativa es ir comentando todas las referencias que haya a *Firebase* a lo largo del proyecto, pero creo que lo primero es más sencillo.

## Developer playlist
Esta es una de las muchas chorradas que se me han ocurrido mientras desarrollaba el proyecto. Aquí iré poniendo los discos, canciones y *playlists* que vaya escuchando mientras pico código, por si alguno quiere venirse arriba y escucharlo también:

- [Oceansize: Frames](https://www.youtube.com/watch?v=nJ2Oj26-0Zs)
- [Casualties Of Cool: Casualties Of Cool](https://www.youtube.com/watch?v=m1vOGSG4Og0)
- [Ween: Quebec](https://www.youtube.com/watch?v=hkX2QATlgbo)
- [Guthrie Govan: Erotic Cakes](https://www.youtube.com/watch?v=JLOE0s9Y7b0)
- [Deftones: Gore](https://www.youtube.com/watch?v=o-3matq2r0Y)
- [Steven Wilson: The Raven That Refused To Sing (And Other Stories)](https://www.youtube.com/watch?v=_w8SY_9yO8k&list=PLgs4tQlTRNY36XH79qSOIeDsTTy-QswEh)
- [†††: Crosses](https://www.youtube.com/watch?v=92dXztDGYdM)
- [Caligula's Horse: The Tide, the Thief & River's End](https://www.youtube.com/watch?v=2f8w-Fowepc&list=PLo2XzIBaNAUILDjJtJ4jy39Rk4VDZYvsp)
- *Más próximamente (o quizá no, depende de la pereza que me de)*
