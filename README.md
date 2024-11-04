#Vinilos App
Este documento detalla los pasos necesarios para configurar, ejecutar y probar la aplicación "Vinilos App", desarrollada en Kotlin para el sistema operativo Android.

## Requisitos del Sistema
Android Studio: Última versión estable.
JDK: Java Development Kit versión 11.
Android SDK: Versiones de Android SDK 30 y superior.
Configuración del Entorno de Desarrollo
Clonar el Repositorio
Inicia clonando este repositorio en tu máquina local:

bash
´´´
git clone https://github.com/tu_usuario/vinillos-app.git
´´´
´´´
cd vinillos-app
´´´

## Abrir el Proyecto en Android Studio
* Inicia Android Studio.
* Selecciona Open an Existing Project.
* Navega y abre la carpeta vinillos-app.
* Espera a que el proyecto sincronice sus dependencias correctamente.

## Ajustes de SDK y JDK
* Dirígete a File -> Project Structure.
* En SDK Location, asegúrate de que JDK location esté configurado para Java 11.
* Verifica que el Android SDK esté configurado para la versión 30 o superior.
* Construcción y Ejecución de la Aplicación


##Construir la Aplicación
* Haz clic en Build -> Make Project para compilar el proyecto.
* Ejecuta la aplicación en un emulador o dispositivo físico mediante Run -> Run 'app'.


##Ejecución de Pruebas


### Pruebas Unitarias
* Las pruebas unitarias están ubicadas en src/test.

###Navega a src/test/java en el panel Project.
* Haz clic derecho en el paquete o archivo de prueba deseado y selecciona Run 'NombrePrueba'.
* Pruebas E2E (End-to-End)
* Las pruebas E2E están situadas en src/androidTest.

### Navega a src/androidTest/java.
* Selecciona el archivo MainActivityTest u otro archivo de prueba.
* Haz clic derecho y selecciona Run 'MainActivityTest'.
* Ejecución de Pruebas a través de Gradle
* Unit Tests: Desde la raíz del proyecto, ejecuta:

bash
´´´
./gradlew test
´´´

E2E Tests: Desde la raíz del proyecto, ejecuta:

bash
´´´
./gradlew connectedAndroidTest
´´´

##Notas Importantes

* Asegúrate de que los emuladores o dispositivos estén correctamente configurados y accesibles.
* Si surge algún problema con las dependencias, verifica los archivos build.gradle.
