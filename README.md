# Charles Proxy: HTTP debugging

Charles es una herramienta que permite interceptar, ver y modificar todo el tráfico HTTP / HTTPS entre la app y el servidor, pudiendo ser la app una aplicación web o mobile que corre en un dispositivo físico o emulador.

***Download:*** https://www.charlesproxy.com/


Cuando el proxy SSL esta habilitado, Charles recibe el certificado del servidor y el browser o aplicacion va a recibir el certificado de Charles. Dado que estos no van a confiar en el certificado es necesario instalar el certificado root en la computadora o dispositivo.

## Setups for Browser Debugging 

### 1. Instalar el certificado root

Abrir Charles e ir al menu:

```sh
Help --> SSL Proxying --> Install Charles Root Certificate
```

![01](img/img01.png)

### 2. Autorizar/Confiar en el certificado

Ir al keychain y buscar el certificado instalado ***Charles Proxy CA*** 

![02](img/img02.png)


luego darle doble click y en la seccion `Confiar` seleccionar `Confiar Siempre`

![03](img/img03.png)

### 3. Reiniciar el Chrome y Charles

<br/>

## Setups for Android Debugging 

### 1. Descargar e Instalar el certificado root en el dispositivo

Abrir el siguiente link en el dispositivo y una vez descargado abrirlo para instalarlo. Si no se descarga el certificado descargarlo desde la computadora y enviarlo de algun modo al dispositivo.

http://chls.pro/ssl

Al instalar el certificado darle un nombre `Charles` y seleccionar `VPN y aplicaciones`

![04](img/img004.png)

### 2. Corroborar que se haya instalado

Para corroborar la correcta instalacion o borrar el certificado puede hacerse desde
```sh
Settings --> Seguridad --> Encriptación y credenciales --> Credenciales del usuario
```

<img src="img/img08.png" width="30%" height="30%">

### 3. Configurar la conexión entre el dispositivo y la computadora

La computadora y el dispositivo deben estar en la misma red por lo tanto o nos aseguramos que esten conectados la misma red WIFI o compartimos internet desde la computadora y nos conectamos a esta red desde el dispositivo mobile.

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

```sh
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

```sh
    <?xml version="1.0" encoding="utf-8"?>
    <manifest ... >
        <application android:networkSecurityConfig="@xml/network_security_config"
                        ... >
            ...
        </application>
    </manifest>
```

### Setups for iPhone Debugging 

<br/>

### Setups for iPhone Simulator Debugging 

## Debugging 

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

Como resultado podrá empezar a ver el trafico entre la aplicación y el servidor.


<img src="img/img12.png" width="50%" height="50%">


### Sobrescribir el body de la respuesta 

```sh
Menu --> Tools  --> Rewrite...
```

#### 1. Agregar/Configurar el request involucrado

<img src="img/img13.png" width="100%" height="100%">

#### 2. Agregarle rules al request

Por ejemplo en este caso se configura para que la aparicion de cierto string en el body sea remplazado por otro.

<img src="img/img14.png" width="100%" height="100%">
