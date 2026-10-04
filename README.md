# T-11-09: laboratorio de CI

Proyecto independiente del servicio Sandbox. Una clase Java y una prueba JUnit 5
se ejecutan con el mismo comando en GitHub Actions y Jenkins: `mvn -B test`.

## Prueba local

```sh
mvn test
```

El reporte XML de Surefire queda en `target/surefire-reports/`. Para provocar
un fallo visible, cambiar en `SumadorTest` el esperado `4` por `5`, confirmar
el cambio y hacer push. Luego restaurar `4` en otro commit y hacer push para
volver al estado verde. Es importante conservar ambos commits y sus corridas;
un fallo de compilación no demuestra la publicación de un reporte JUnit fallido.

## GitHub Actions

El archivo `.github/workflows/maven-test.yml` se activa con cada `push`.
El job `test` corre en un runner Ubuntu, prepara Java 21, ejecuta Maven y
adjunta el XML de Surefire aunque el test falle. El workflow no requiere secrets.

## Jenkins local

Desde este directorio:

```sh
docker compose up -d --build
```

Abrir `http://localhost:8085` en el propio equipo. Crear un item de tipo
**Pipeline** llamado `lab-ci-t11-09`. En **Pipeline → Definition**, elegir
**Pipeline script from SCM**; seleccionar Git, indicar la URL del repositorio
de este laboratorio y dejar `Jenkinsfile` como **Script Path**. Ejecutar
**Build Now** en el commit verde, luego otra vez después del push rojo. En cada
build, ver **Test Result**; el reporte se publica desde
`target/surefire-reports/*.xml` incluso si `mvn test` termina con error.
La configuración del job utilizada para esta evidencia está en
`jenkins/job-config.xml`.

Jenkins se expone solo en `127.0.0.1:8085` y la imagen desactiva el asistente
inicial exclusivamente para este laboratorio local. No usar este Compose como
instalación compartida ni publicarlo en Internet. Detenerlo al terminar con
`docker compose down`.

## Conceptos para explicar el 09/10

| Concepto | En este laboratorio |
|---|---|
| Workflow / pipeline | Receta automatizada: YAML en Actions, `Jenkinsfile` en Jenkins. |
| Job | Unidad que recibe un runner; aquí el job `test` o el build de Jenkins. |
| Step / stage | Paso del job en Actions; etapa `Obtener codigo` o `Probar con Maven` en Jenkins. |
| Runner / agente | Máquina o proceso donde se ejecuta el job. Actions usa Ubuntu hospedado; Jenkins usa su contenedor local como agente para este ejercicio. |
| Trigger | Evento que inicia la ejecución. Aquí `push` en Actions y **Build Now** manual en Jenkins. También existen PR, invocación manual y API. |
| Artefacto | Archivo conservado después del job. Actions adjunta el XML; Jenkins publica el resultado JUnit en el build. |
| Secret | Dato sensible que el CI entrega al job mediante su gestor de credenciales. Este lab no necesita ninguno. |
| Cache | Dependencias reutilizadas entre corridas para reducir tiempo. `setup-java` habilita la caché Maven de Actions. |

Si la aserción falla, Surefire escribe el XML con el fallo y Maven sale con código
distinto de cero. El step de Actions y el `sh` de Jenkins interpretan ese código
como fallo, por eso el job se ve rojo. El bloque `post { always { junit ... } }`
corre de todos modos y permite inspeccionar cuál test falló.

## Evidencia de la tarea

- Repositorio: https://github.com/412348-MIARKA/lab-jenkins
- Actions verde (commit `0fca988`): https://github.com/412348-MIARKA/lab-jenkins/actions/runs/37210223521
- Actions rojo provocado (commit `27984ee`): https://github.com/412348-MIARKA/lab-jenkins/actions/runs/37210547926
- Jenkins verde local: http://127.0.0.1:8085/job/lab-ci-t11-09/1/
- Jenkins rojo local: http://127.0.0.1:8085/job/lab-ci-t11-09/2/

Los enlaces de Jenkins solo funcionan en la máquina donde corre Docker. En
esta entrega, el job `#1` terminó `SUCCESS` con un test sin fallas y el `#2`
terminó `FAILURE` con un test fallido publicado en **Resultado de los tests**.
