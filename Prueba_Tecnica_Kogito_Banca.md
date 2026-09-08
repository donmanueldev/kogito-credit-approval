# Prueba Técnica — Kogito / Spring Boot

## Contexto

Se requiere implementar un proceso simplificado para evaluar solicitudes de crédito utilizando **Kogito, Spring Boot, BPMN y DMN**.

El objetivo de la prueba es evaluar la capacidad del candidato para modelar procesos y reglas de negocio, implementar decisiones con Kogito y explicar las decisiones técnicas tomadas.

El proyecto base será proporcionado al candidato.

---

## Caso de uso

El sistema debe recibir una solicitud de crédito con la siguiente información:

```json
{
  "customerId": "CUST-001",
  "creditScore": 720,
  "monthlyIncome": 2500,
  "monthlyDebt": 600,
  "requestedAmount": 10000,
  "fraudConfirmed": false
}
```

La relación deuda/ingreso debe calcularse como:

```text
debtRatio = monthlyDebt / monthlyIncome
```

El resultado de la evaluación debe ser uno de los siguientes:

- `APROBADO`
- `REVISION_MANUAL`
- `RECHAZADO`

Para esta prueba no se debe utilizar `400 Bad Request` como resultado de negocio. Si faltan datos o contienen valores inválidos, el proceso debe finalizar con `RECHAZADO` y una razón explícita, por ejemplo `DATOS_INVALIDOS`.

---

## Reglas de negocio

1. Si existe fraude confirmado, el resultado debe ser **RECHAZADO**, independientemente de cualquier otra condición.
2. Si `creditScore >= 750`, `monthlyIncome >= 1500` y `debtRatio <= 35%`, el resultado debe ser **APROBADO**.
3. Si `creditScore` está entre `650` y `749`, la solicitud debe pasar a **REVISION_MANUAL**.
4. Si `creditScore < 650`, el resultado debe ser **RECHAZADO**.
5. Los datos inválidos o incompletos deben manejarse correctamente y no provocar errores inesperados.

Cuando el fraude esté confirmado, debe conservar prioridad también en la razón devuelta. Para evitar una división entre cero, `monthlyIncome = 0` no debe calcular `debtRatio` y debe conducir a `RECHAZADO` por datos inválidos.

---

## Tareas

### 1. Modelar las reglas con DMN

Crear una decisión DMN que determine el resultado de la solicitud.

El candidato debe definir correctamente:

- Entradas y salidas.
- Expresiones FEEL.
- Hit Policy.
- Límites de las reglas.
- Prioridad de fraude.

Debe poder explicar las decisiones tomadas durante el modelado.

### 2. Modelar el proceso con BPMN

Crear un proceso BPMN que represente como mínimo:

```text
Solicitud recibida
        ↓
Validar datos
        ↓
Evaluar fraude / reglas
        ↓
Ejecutar decisión
        ↓
Aprobado | Revisión manual | Rechazado
        ↓
Finalizar proceso
```

Se evaluará especialmente la separación entre el proceso BPMN y las decisiones de negocio DMN.

### 3. Exponer la evaluación

El proceso debe poder iniciarse mediante HTTP utilizando las capacidades de Kogito/Spring Boot.

El endpoint esperado es el generado por Kogito para el proceso, con la ruta `POST /creditApproval`.

No se requiere frontend, autenticación ni persistencia en base de datos.

### 4. Manejar casos límite

La solución debe contemplar escenarios como:

- `creditScore = 650`
- `creditScore = 749`
- `creditScore = 750`
- `debtRatio = 35%`
- `monthlyIncome = 0`
- Valores requeridos nulos.
- Solicitud incompleta, por ejemplo `{}`.
- Fraude confirmado con un score que normalmente sería aprobado.
- Fraude confirmado con otros datos inválidos.

### 5. Pruebas automatizadas

Agregar pruebas automatizadas para demostrar el comportamiento de las reglas principales y sus límites.

Como mínimo deben comprobarse escenarios de:

- Aprobación automática.
- Revisión manual.
- Rechazo.
- Fraude.
- Valores límite.
- Solicitud incompleta y ratio no calculable.

---

## Entregable

El candidato debe entregar el proyecto funcionando junto con un `README.md` que incluya:

- Instrucciones para ejecutar el proyecto.
- Ejemplo para ejecutar la evaluación.
- Breve explicación de la solución.
- Hit Policy seleccionada y justificación.
- Supuestos realizados.
- Qué cambios realizaría para llevar esta solución a un entorno productivo bancario.

El README debe explicar también cómo se separan la orquestación BPMN y las reglas DMN, qué hit policy se eligió y qué supuesto se tomó para un score `>= 750` que no cumple ingreso o ratio de deuda.

---

## Criterios de evaluación

| Área | Peso |
|---|---:|
| DMN / FEEL / reglas de negocio | 30% |
| BPMN / modelado del proceso | 25% |
| Integración Kogito + Spring Boot | 15% |
| Testing | 15% |
| Explicación técnica | 10% |
| Calidad y claridad del código | 5% |

> La prueba busca evaluar principalmente el conocimiento práctico de **Kogito, BPMN, DMN, FEEL, motores de reglas y diseño de procesos**, así como la capacidad del candidato para explicar y defender técnicamente su solución.
