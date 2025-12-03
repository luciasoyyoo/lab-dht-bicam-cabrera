# Proyecto BiCIAM 
## Instrucciones de compilación y notas

Resumen: 
Este repositorio contiene la versión del proyecto BiCIAM organizada para compilarse con Maven.

### Compilar y empaquetar

Abre un terminal en la carpeta del módulo `practica5` (el POM está en `practica5/pom.xml`) y ejecuta:

```bash
cd practica5
mvn -DskipTests package
```

Para sólo compilar (sin empaquetar):

```bash
mvn -DskipTests compile
```

### Codificación de fuentes

Las fuentes Java originales contienen caracteres acentuados con codificación ISO-8859-1. Por compatibilidad el `pom.xml` actualmente usa:

  <project.build.sourceEncoding>ISO-8859-1</project.build.sourceEncoding>

Recomendaciones:
- Si quieres normalizar el proyecto a UTF-8 (recomendado a largo plazo), reencodifica los `.java` a UTF-8 y luego cambia la propiedad en el POM a `UTF-8`.
- Si prefieres no tocar los archivos, mantener `ISO-8859-1` en el POM es válido y compila correctamente.

Cómo reencodificar (ejemplo, haz backup o usa git):

```bash
# desde la raíz del repo, reencodificar todos los .java (prueba primero en un par de ficheros)
find . -name "*.java" -print0 | xargs -0 -n1 -I{} bash -c 'iconv -f ISO-8859-1 -t UTF-8 "{}" -o "{}".utf8 && mv "{}".utf8 "{}"'
# luego actualiza el POM a UTF-8
# git add -A && git commit -m "Reencodificar fuentes a UTF-8"
```

Dependencias importantes

- Se añadió `net.sourceforge.jexcelapi:jxl` para que las clases que usan `jxl.read.biff.BiffException` compilen.

Siguientes pasos sugeridos

- Considerar actualizar `maven.compiler.source`/`target` a una versión más nueva si tu JDK lo permite (por ejemplo `1.8` o superior).
- Habilitar `-Xlint` en el plugin del compilador para ver avisos sobre APIs obsoletas.
- Confirmar que el proyecto se ejecuta correctamente (tests, usos manuales).

Contacto

Si quieres, puedo:
- Reencodificar automáticamente todas las fuentes a UTF-8 y actualizar el POM.
- Crear un commit con estos cambios.
- Añadir un script de comprobación/CI para compilar en cada push.

## Usar SpotBugs (análisis estático)

SpotBugs es una herramienta de análisis estático que detecta bugs, problemas de rendimiento y malas prácticas en código Java. Este proyecto ya tiene configurado el plugin de SpotBugs en el `pom.xml`, por lo que puedes ejecutarlo con Maven.

- Ejecutar SpotBugs (genera informes en `target/`):

```bash
cd practica5
mvn spotbugs:spotbugs
```

- Ejecutar la comprobación que puede fallar la build si hay bugs (usa la misma configuración que el `pom`):

```bash
cd practica5
mvn spotbugs:check
```

- Ver informes generados:
  - `target/spotbugsXml.xml` — informe XML
  - `target/spotbugs.html` o `target/spotbugs.html` (si está habilitado en tu configuración) — informe HTML (si el plugin genera HTML en tu configuración)
  - `target/spotbugs.xml` — otro formato posible según versión/configuración

- Abrir la GUI de SpotBugs para explorar los resultados interactivamente (útil para depurar y navegar las advertencias):

```bash
cd practica5
mvn spotbugs:gui
```

Notas y buenas prácticas
- El análisis puede producir muchas advertencias (naming, style, performance, etc.). Clasifica los hallazgos en: bugs reales (fix urgente), falsos positivos, y code-style (mejorables pero no bloqueantes).
- Para ignorar problemas conocidos puedes usar un filtro XML de SpotBugs (ver plugin docs) o suprimir con anotaciones/comentarios cuando corresponda.
- Recomendable: ejecutar `mvn -DskipTests verify` localmente y luego `mvn spotbugs:check` antes de crear un PR para reducir ruido.

Si quieres, puedo:
- Generar un informe HTML consolidado y añadir un enlace al README.
- Crear un filtro SpotBugs para silenciar falsos positivos conocidos.


## Comandos Maven útiles

Estos son los comandos Maven más usados en este proyecto. Ejecuta desde la raíz del módulo `practica5` (o añade `-f practica5/pom.xml` si ejecutas desde la raíz del repo).

```bash
# situarse en el módulo
cd practica5

# compilar y empaquetar (salta tests)
mvn -DskipTests package

# ejecutar los tests unitarios
mvn test

# ejecutar la fase de verificación (incluye pruebas y otros checks configurados)
mvn verify

# instalar el artefacto localmente en el repositorio Maven (~/.m2/repository)
mvn install

# limpiar el directorio target
mvn clean

# compilar sólo
mvn compile

# ejecutar SpotBugs (análisis estático)
mvn spotbugs:spotbugs

# abrir la GUI de SpotBugs (explorar resultados interactivamente)
mvn spotbugs:gui

# si ejecutas desde la raíz del repo sin cambiar de carpeta
mvn -f practica5/pom.xml test
```

### Consejos rápidos:
- Para builds CI/automatizados suele usarse `mvn -B verify` (modo batch para evitar prompts).
- Añade `-DskipTests` si necesitas compilar/empquetar rápido y los tests no son necesarios en ese momento.


## Análisis con SonarCloud

Para enviar el análisis a SonarCloud necesitas:

- Tener el informe de cobertura JaCoCo en `target/site/jacoco/jacoco.xml` (genera con JaCoCo).
- Un token de usuario/organización en SonarCloud (no lo incluyas en el repositorio).

Ejemplo de flujo (desde `practica5`):

1. Generar los tests y el informe JaCoCo:

```bash
cd practica5
# genera tests y el informe XML de JaCoCo (ajusta según tu configuración de JaCoCo en el POM)
mvn clean test jacoco:report
```

2. Exportar el token como variable de entorno (no lo metas en ficheros del repo):

```bash
export SONAR_TOKEN=tu_token_aqui
```

3. Ejecutar el análisis en SonarCloud (uso de variable para el token):

```bash
mvn sonar:sonar \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.login="$SONAR_TOKEN" \
  -Dsonar.organization=luciasoyyoo \
  -Dsonar.projectKey=luciasoyyoo_lab-dht-bicam-cabrera \
  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
```

Nota de seguridad: nunca comitees el token en el repositorio. En CI/servicios remotos (GitHub Actions, GitLab CI, etc.) guarda el token en el gestor de secretos y pásalo como variable de entorno.

Si prefieres, puedes usar `-Dsonar.token=$SONAR_TOKEN` en lugar de `-Dsonar.login` (ambas opciones funcionan, pero `sonar.login` es la más común en ejemplos).

