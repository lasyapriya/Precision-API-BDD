# 🐾 Precision API BDD — Automated API Testing Framework

> A production-grade BDD test automation framework built for the **Veeva Systems Fresh Graduate Assignment**.  
> Powered by **Java · REST Assured · Cucumber · Maven · Log4J**  
> Target API: [PetStore Swagger v2](https://petstore.swagger.io/v2)

---

## 📌 Table of Contents

- [Project Overview](#-project-overview)
- [Tech Stack](#-tech-stack)
- [Framework Architecture](#-framework-architecture)
- [Project Structure](#-project-structure)
- [Test Scenarios](#-test-scenarios)
- [Prerequisites](#-prerequisites)
- [How to Run](#-how-to-run)
- [Reports](#-reports)
- [Postman Collection](#-postman-collection)
- [Team Members & Roles](#-team-members--roles)

---

## 🧭 Project Overview

**Precision API BDD** is an end-to-end API test automation framework designed to validate the PetStore REST API across four comprehensive test scenarios. The framework follows a **Hybrid BDD Architecture** — combining clean separation of concerns (Client Pattern, Utility Layer, Config Management) with Cucumber's Gherkin-based BDD approach.

The goal wasn't just to write tests — it was to build something **reusable, maintainable, and scalable**, exactly how it's done in real projects.

| Property | Value |
|---|---|
| **Base URL** | `https://petstore.swagger.io/v2` |
| **API Docs** | https://petstore.swagger.io |
| **GitHub Repo** | https://github.com/lasyapriya/Precision-API-BDD |
| **Team Size** | 2 Members |
| **Execution Command** | `mvn clean test` |

---

## 🛠 Tech Stack

| Tool | Version | Purpose |
|---|---|---|
| Java (JDK) | 11+ | Core language for all test logic and step definitions |
| REST Assured | 5.x | Fluent HTTP request building and response validation |
| Cucumber | 7.x | BDD framework — parses feature files and maps steps to Java |
| Maven | 3.8+ | Build tool, dependency manager, test execution lifecycle |
| JUnit | 4.x | Test runner integrated with Cucumber |
| Log4J 2 | 2.x | Structured logging of requests, responses, and assertions |
| IntelliJ IDEA | Latest | Primary IDE with Cucumber and Gherkin plugin support |
| Git / GitHub | — | Version control and remote repository hosting |
| Postman | Latest | Manual API exploration and collection-based testing |

---

## 🏗 Framework Architecture

The framework follows a layered hybrid design. Here's how everything connects at runtime:

```
Feature Files (.feature)
        ↓
Cucumber Engine (parses Gherkin, resolves steps)
        ↓
Step Definitions (Java — binds Gherkin steps to logic)
        ↓
Client Layer: PetClient / StoreClient / UserClient (REST Assured)
        ↓
PetStore API (HTTPS — live public API)
        ↓
Assertions + ScenarioContext (shared state between steps)
        ↓
Log4J (logs every request/response) + Cucumber HTML Report + Surefire
```

**Key Design Decisions:**

- **Client Pattern** — PetClient, StoreClient, and UserClient each own their API domain. Step definitions don't build HTTP requests directly — they delegate to clients. This keeps step definitions readable and the HTTP layer reusable.
- **ScenarioContext** — A shared thread-safe context object passed via Cucumber's dependency injection. This is how we chain responses across steps (e.g., the pet ID created in Step 1 is accessible in Steps 2–5).
- **Hooks** — `@Before` sets up logging and context; `@After` logs scenario outcome and cleans state.
- **ConfigReader** — All environment-specific values (base URL, timeouts) live in `config.properties`. Nothing is hardcoded in test code.

> 📎 See the Architecture Diagram in `/docs/architecture-diagram.png`

---

## 📁 Project Structure

```
Precision-API-BDD/
├── src/
│   └── test/
│       ├── java/
│       │   └── com/precision/
│       │       ├── runner/
│       │       │   └── TestRunner.java              ← Cucumber JUnit runner
│       │       ├── stepdefinitions/
│       │       │   ├── PetStepDefs.java             ← TC1 & TC4 step bindings
│       │       │   ├── InventoryStepDefs.java        ← TC2 step bindings
│       │       │   └── UserStepDefs.java             ← TC3 step bindings
│       │       ├── client/
│       │       │   ├── PetClient.java               ← POST/GET/PUT/DELETE /pet
│       │       │   ├── StoreClient.java             ← GET /store/inventory
│       │       │   └── UserClient.java              ← POST/GET /user, GET /user/login
│       │       ├── utils/
│       │       │   ├── ConfigReader.java            ← Reads config.properties
│       │       │   ├── ScenarioContext.java         ← Shared state between steps
│       │       │   └── LoggerUtil.java              ← Log4J wrapper
│       │       └── hooks/
│       │           └── Hooks.java                   ← @Before / @After Cucumber hooks
│       └── resources/
│           ├── features/
│           │   ├── PetLifecycle.feature             ← TC1: CRUD & response chaining
│           │   ├── InventoryAnalysis.feature        ← TC2: Complex data parsing
│           │   ├── UserSecurity.feature             ← TC3: Negative & error handling
│           │   └── DataConsistency.feature          ← TC4: Cross-endpoint validation
│           ├── config.properties                    ← Base URL and environment config
│           └── log4j2.xml                           ← Logging configuration
├── postman/
│   └── PrecisionAPI.postman_collection.json         ← Exported Postman collection
├── target/
│   ├── cucumber-reports/                            ← HTML execution report
│   └── surefire-reports/                            ← Maven Surefire XML/HTML
├── pom.xml
└── README.md
```

---

## 🧪 Test Scenarios

### TC1 — The Lifecycle of a Pet *(CRUD & Response Chaining)*

Creates a pet, reads it back, updates its status, deletes it, and confirms it's gone. The pet ID from the POST response is chained through all subsequent steps using `ScenarioContext`.

| Step | Method | Endpoint | Validation |
|---|---|---|---|
| 1 | POST | `/pet` | Pet created, ID extracted |
| 2 | GET | `/pet/{petId}` | Name and status match initial values |
| 3 | PUT | `/pet` | Status updated to `sold` — 200 OK |
| 4 | DELETE | `/pet/{petId}` | Deletion returns 200 |
| 5 | GET | `/pet/{petId}` | Returns 404 — pet no longer exists |

---

### TC2 — Inventory Analysis *(Complex Data Parsing)*

Validates that two completely independent endpoints agree on the count of available pets.

| Step | Method | Endpoint | Validation |
|---|---|---|---|
| 1 | GET | `/store/inventory` | Parse JSON map, extract `available` count |
| 2 | GET | `/pet/findByStatus?status=available` | Count items in the returned list |
| — | Cross-check | Both endpoints | Both counts must match |

> Uses Java Streams to count and compare the `available` values across both response types.

---

### TC3 — User Security & Error Handling *(Negative Testing)*

Deliberately sends bad input and nonexistent identifiers to verify the API responds with the right errors. Also confirms that failed login attempts don't return a usable session token.

| Step | Scenario | Expected |
|---|---|---|
| 1 | POST `/user` with invalid email format | API creates user (no email validation in PetStore) |
| 2 | GET `/user/nonExistentUser123` | 404 + message contains `"User not found"` |
| 3 | GET `/user/login` with wrong credentials | Response must NOT contain a valid session token |

---

### TC4 — Cross-Endpoint Data Consistency

Creates a uniquely identified pet, updates its status to `sold`, and verifies the change is reflected consistently across two separate endpoints — inventory map and status-filtered list.

| Step | Method | Endpoint | Purpose |
|---|---|---|---|
| 1 | POST | `/pet` | Create pet with category `"HighValue-Bulldog"`, status `available` |
| 2 | PUT | `/pet` | Update status from `available` → `sold` |
| 3 | GET | `/store/inventory` | Confirm `sold` count has increased |
| 4 | GET | `/pet/findByStatus?status=sold` | Locate the specific pet ID using Java Streams |

---

## ✅ Prerequisites

Before running the framework, make sure you have the following installed:

- **Java JDK 11+** — verify with `java -version`
- **Apache Maven 3.8+** — verify with `mvn -version`
- **Git** — verify with `git --version`
- **IntelliJ IDEA** (Community or Ultimate)
  - Install plugins: **Cucumber for Java** and **Gherkin** (File → Settings → Plugins)
- Active internet connection (PetStore API is a live public API)

---

## ▶ How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/lasyapriya/Precision-API-BDD.git
cd Precision-API-BDD
```

### 2. Open in IntelliJ

1. File → Open → select the cloned folder
2. IntelliJ detects `pom.xml` — click **Trust Project** when prompted
3. Open the **Maven panel** (right sidebar) → click the **Reload All Maven Projects** icon
4. Wait for all dependencies to download (first-time only, takes ~2 minutes)

### 3. Run via Maven *(Recommended)*

```bash
# Run the complete test suite
mvn clean test

# Run a specific tagged scenario
mvn clean test -Dcucumber.filter.tags="@PetLifecycle"

# Run only negative tests
mvn clean test -Dcucumber.filter.tags="@NegativeTest"

# Run TC4 only
mvn clean test -Dcucumber.filter.tags="@DataConsistency"
```

### 4. Run via IntelliJ

- Navigate to `src/test/java/com/precision/runner/TestRunner.java`
- Right-click → **Run 'TestRunner'**
- Or right-click any `.feature` file → **Run Feature**

---

## 📊 Reports

After running `mvn clean test`, reports are generated automatically:

| Report | Location | How to View |
|---|---|---|
| **Cucumber HTML Report** | `target/cucumber-reports/index.html` | Open in any browser |
| **Maven Surefire Report** | `target/surefire-reports/*.html` | Open in any browser |
| **Log4J Execution Log** | `logs/test-execution.log` | Open in any text editor |

The Cucumber HTML report shows a full breakdown of each scenario — which steps passed, which failed, how long each took, and the full stack trace for any failures.

---

## 📬 Postman Collection

A complete Postman collection is included in the `/postman` folder:

**File:** `postman/PrecisionAPI.postman_collection.json`

The collection covers all four Postman assignments:

| Assignment | Endpoint | What It Tests |
|---|---|---|
| 1 | GET `/pet/findByStatus` | Query param `status=available`, assert 200 |
| 2 | POST `/pet` | Create pet with body, assert name in response |
| 3 | GET/POST various | Status code verification: 200, 404, 400/405 |
| 4 | All requests | Environment variable `{{url}}` replacing base URL |

### Setting up the Postman Environment

1. Open Postman → **Environments** → **New**
2. Name it `PetStore-Env`
3. Add variable: `url` = `https://petstore.swagger.io/v2`
4. Import the collection from `/postman/PrecisionAPI.postman_collection.json`
5. Select `PetStore-Env` as the active environment
6. All requests will now use `{{url}}/pet`, `{{url}}/store/inventory`, etc.

---

## 👥 Team Members & Roles

| Member | Contributions |
|---|---|
| **Divya** | Project setup, Maven/pom.xml configuration, BaseTest, PetClient, StoreClient, ScenarioContext, TC1 Pet Lifecycle, TC2 Inventory Analysis, Log4J configuration, Git setup and version control |
| **P. Lasya Priya** | UserClient, TC3 User Security & Error Handling, TC4 Cross-Endpoint Data Consistency, Postman collection (all 4 assignments), Architecture diagram, README documentation |

Both members participated in code reviews, debugging, and final integration testing.

---

## 📎 Notes

- The PetStore API is a **shared public API** — other users are also creating/modifying/deleting pets at the same time. This means:
  - Inventory counts in TC2 may have a small tolerance margin (±5) due to concurrent usage
  - Occasionally a pet created in TC1 may already be gone if the API resets between steps
  - All of this is expected behavior for a public sandbox API and does not indicate a framework issue
- All test data (pet names, statuses, usernames) is passed dynamically through **Cucumber Examples tables** — nothing is hardcoded in step definitions

---

*Built with passion for the Veeva Systems Fresh Graduate Assignment*
