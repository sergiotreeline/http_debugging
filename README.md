# HTTP debugging: Charles Proxy

Charles es una herramienta que permite interceptar, ver y modificar todo el tráfico HTTP / HTTPS entre la app y el servidor, pudiendo ser la app una aplicación web o mobile que corre en un dispositivo físico o emulador.

***Download:*** https://www.charlesproxy.com/

Cuando el proxy SSL está habilitado, Charles recibe el certificado del servidor y el browser o aplicación va a recibir el certificado de Charles. Dado que estos no van a confiar en el certificado es necesario instalar el certificado root en la computadora o dispositivo.

- [Setups for Browser Debugging](#setups-for-browser-debugging)
- [Setups for Android Debugging](#setups-for-android-debugging)
- [Setups for iPhone Debugging](#setups-for-iphone-debugging)
- [Setups for iPhone Simulator Debugging](#setups-for-iphone-simulator-debugging)
- [Start Debugging:](#start-debugging)
  - [Setup Charles](#setup-charles)
  - [Modificar las respuestas](#modificar-las-respuestas)
  - [Agregar delay a las respuestas](#agregar-delay-a-las-respuestas)
  - [Ejecutar un request repetidamente](#ejecutar-un-request-repetidamente)
  - [Breakpoints](#breakpoints)

<br/>

## Setups for Browser Debugging 

### 1. Instalar el certificado root

Abrir Charles e ir al menú:

```sh
Help --> SSL Proxying --> Install Charles Root Certificate
```

![01](img/img01.png)

### 2. Autorizar/Confiar en el certificado

Ir al keychain y buscar el certificado instalado ***Charles Proxy CA*** 

![02](img/img02.png)


luego darle doble click y en la sección `Confiar` seleccionar `Confiar Siempre`

![03](img/img03.png)

### 3. Reiniciar el Chrome y Charles

<br/>

## Setups for Android Debugging 

### 1. Descargar e Instalar el certificado root en el dispositivo

Abrir el siguiente link en el dispositivo y una vez descargado abrirlo para instalarlo. Si no se descarga el certificado descargarlo desde la computadora y enviarlo de algún modo al dispositivo.

http://chls.pro/ssl

Al instalar el certificado darle un nombre `Charles` y seleccionar `VPN y aplicaciones`

![04](img/img004.png)

### 2. Corroborar que se haya instalado

Para corroborar la correcta instalación o borrar el certificado puede hacerse desde
```sh
Settings --> Seguridad --> Encriptación y credenciales --> Credenciales del usuario
```

<img src="img/img08.png" width="30%" height="30%">

### 3. Configurar la conexión entre el dispositivo y la computadora

La computadora y el dispositivo deben estar en la misma red por lo tanto o nos aseguramos que estén conectados la misma red WIFI o compartimos internet desde la computadora y nos conectamos a esta red desde el dispositivo mobile.

### 4. Configurar proxy

Obtener la IP de la computadora:

```sh
ifconfig | grep "inet " | grep -Fv 127.0.0.1 | awk '{print $2}'
```

Para configurar el Proxy se debe ir en el dispositivo a:

```sh
Settings  --> Internet y redes --> Seleccionar la red WiFi --> Settings --> Edit
```
 Dentro de opciones avanzadas en la sección proxy seleccionar manual y en `host` ingresar la IP de la computadora y en puerto `8888`

![06](img/img06.png)

<br/>

### 5. Configurar Aplicación Android

***Source:*** https://developer.android.com/training/articles/security-config

#### Agregar en el proyecto Android el archivo `res/xml/network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config>
        <trust-anchors>
            <!-- Trust preinstalled CAs -->
            <certificates src="system" />
            <!-- Additionally trust user added CAs -->
            <certificates src="user" />
        </trust-anchors>
    </base-config>
</network-security-config>
```


#### Agregar el archivo de configuración de seguridad de red en el manifest

```xml
    <?xml version="1.0" encoding="utf-8"?>
    <manifest ... >
        <application android:networkSecurityConfig="@xml/network_security_config"
                        ... >
            ...
        </application>
    </manifest>
```
<br/>

## Setups for iPhone Debugging 

### 1. Descargar e Instalar el certificado root en el dispositivo

Abrir el siguiente link en el iPhone para descargar el certificado

http://chls.pro/ssl

Luego ir a la siguiente sección para instalarlo

```sh
Settings --> General --> Profiles & Device Management --> Selecionar certificado --> Instalar
```

![](img/img22.png)

### 2. Habiltar certificado

Ir a la siguiente sección y habilitar el certificado:

```sh
Settings --> General --> About --> Certificate Trust Settings
```

<img src="img/img023.png" width="50%" height="50%">

### 3. Configurar la conexión entre el dispositivo y la computadora

La computadora y el dispositivo deben estar en la misma red por lo tanto o nos aseguramos que estén conectados la misma red WIFI o compartimos internet desde la computadora y nos conectamos a esta red desde el dispositivo mobile.

### 4. Configurar proxy

Obtener la IP de la computadora:

```sh
ifconfig | grep "inet " | grep -Fv 127.0.0.1 | awk '{print $2}'
```

Para configurar el Proxy se debe ir en el dispositivo a:

```sh
Settings  --> Wi Fi --> Info button --> Configure Proxy 
```
e ingresar la IP de la computadora y en puerto `8888`

![](img/img024.png)

<br/>

## Setups for iPhone Simulator Debugging 

Instalar el certificado en el simulador desde el menú de charles:

```sh
Menu --> Help  --> SSL Proxying --> Install Charles Root Certificate in iOS Simulator
```

<img src="img/img21.png" width="50%" height="50%">

`A veces es necesario reiniciar el simulador`

<br/>

## Start Debugging 



### Setup Charles

#### 1. Configurar el puerto `8888`

```sh
Menu --> Proxy  --> Proxy Settings
```

![](img/img10.png)

#### 2. Agregar el host que se quiere inspeccionar en:

```sh
Menu --> Proxy  --> SSL Proxying Settings
```

![](img/img09.png)


#### 3. Start SSL:

```sh
Menu --> Proxy  --> Start SSL Proxying
```

<img src="img/img11.png" width="50%" height="50%">

Como resultado podrá empezar a ver el tráfico entre la aplicación y el servidor.

<img src="img/img12.png" width="50%" height="50%">


### Modificar las respuestas

Para poder modificar la respuesta de un request se debe ir a:
```sh
Menu --> Tools  --> Rewrite...
```

***Se pueden crear rules para:***
* Add/Modify/Remove Header
* Host
* Path URL
* Add/Modify/Remove Query Params
* Response Status
* Body

#### 1. Agregar/Configurar el request involucrado

<img src="img/img13.png" width="100%" height="100%">

#### 2. Agregarle rules al request para que sobreescriba parte del body

Por ejemplo en este caso se configura para que la aparición de cierto string en el body sea reemplazado por otro.

<img src="img/img14.png" width="100%" height="100%">

Como se puede observar se recibirá el valor configurado

<img src="img/img15.png" width="100%" height="100%">

#### 3. Agregarle rules al request para modificar el status code

También se puede editar el status code, esto puede útil para testear diferentes casos de uso como por ejemplo podría ser la expiración de la sesión.

<img src="img/img16.png" width="50%" height="50%">

### Agregar delay a las respuestas


Ir a la siguiente sección:

```sh
Menu --> Proxy  --> Throttle Settings...
```
 
y configurar el host en `Location` y agregar una latencia al request.

<img src="img/img17.png" width="50%" height="50%">

Por último habilitar la funcionalidad desde:

```sh
Menu --> Proxy  --> Start Throttling
```

### Ejecutar un request repetidamente

Desde la siguiente sección se puede ejecutar un request de forma concurrente y repetida, esto puede servir para ver cómo se comporta el servidor ante cierta carga.

```sh
Menu --> Tools  --> Advanced Repeat...
```

<img src="img/img18.png" width="50%" height="50%">

### Breakpoints

Los breakpoints permiten interceptar los request o response y `editarlos on the fly`. Para esto es necesario configurarlos desde:

```sh
Menu --> Proxy  --> Breakpoint Settings...
```

Se debe configurar el location:

<img src="img/img19.png" width="100%" height="100%">

y por ultimo habilitar los breakpoints:

```sh
Menu --> Proxy  --> Enable Breakpoints
```

Una vez hecho esto cada vez que se ejecute el request Charles nos notificará y no se permitirá editarlo.

Algo importante a tener en cuenta es que editarlo puede llevarnos un tiempo por ende la aplicación podría llegar a dar timeout, por eso mismo es probable que antes haya que setear en la aplicación un timeout alto.

<img src="img/img20.png" width="100%" height="100%">
