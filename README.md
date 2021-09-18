# Charles Proxy: HTTP debugging

Charles es una herramienta que permite interceptar, ver y modificar todo el tráfico HTTP / HTTPS entre la app y el servidor, pudiendo ser la app una aplicación web o mobile que corre en un dispositivo físico o emulador.

***Download:*** https://www.charlesproxy.com/


Cuando el proxy SSL esta habilitado, Charles recibe el certificado del servidor y el browser o aplicacion va a recibir el certificado de Charles. Dado que estos no van a confiar en el certificado es necesario instalar el certificado root en la computadora o dispositivo.

### Setups for Browser Debugging 

#### 1. Instalar el certificado root

Abrir Charles e ir al menu:

```sh
Help --> SSL Proxying --> Install Charles Root Certificate
```

#### 2. Autorizar/Confiar en el certificado

Ir al keychain y buscar el certificado instalado ***Charles Proxy CA*** luego darle doble click y en la seccion `Confiar` seleccionar `Confiar Siempre`


### Setups for Android Debugging 

### Setups for iPhone Debugging 

