# 🏦 Desafio BIP – Gestão de Benefícios

Aplicação full-stack para cadastro e transferência de valores entre benefícios.

Projeto dividido em:

* **Backend:** Spring Boot 3 (Java 17, JPA, H2, Swagger/OpenAPI)
* **Frontend:** Angular 15
* **Banco:** H2 (file database)

---

# 📐 Arquitetura

```
desafio-bip
 ├── backend-module
 └── frontend
```

Backend expõe API REST para:

* CRUD de benefícios
* Transferência de valores entre benefícios
* Documentação via Swagger/OpenAPI

Frontend consome a API e permite:

* Listagem
* Criação / edição
* Transferência entre benefícios
* Exibição de erros via toast

---

# 🚀 Como Executar o Projeto

## 🔹 Backend

### Pré-requisitos

* Java 17
* Maven 3.9+

### Executar

```bash
cd backend-module
mvn clean install
mvn spring-boot:run
```

Backend sobe em:

```
http://localhost:8080
```

### Console H2

```
http://localhost:8080/h2-console
```

JDBC URL:

```
jdbc:h2:file:./data/bipdb
```

---

## 🔹 Frontend

### Pré-requisitos

* Node 18+
* Angular CLI 15

### Executar

```bash
cd frontend
npm install
ng serve
```

Frontend disponível em:

```
http://localhost:4200
```

---

# 📚 Documentação da API (Swagger / OpenAPI)

Com o backend rodando:

### 🔹 Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

Interface interativa para testar endpoints.

### 🔹 OpenAPI JSON

```
http://localhost:8080/v3/api-docs
```

Especificação completa da API no padrão OpenAPI 3.

---

# 📌 Endpoints Principais

## Benefícios

| Método | Endpoint                      | Descrição                 |
| ------ | ----------------------------- | ------------------------- |
| GET    | `/api/v1/beneficios`          | Lista todos os benefícios |
| POST   | `/api/v1/beneficios`          | Cria benefício            |
| PUT    | `/api/v1/beneficios/{id}`     | Atualiza benefício        |
| POST   | `/api/v1/beneficios/transfer` | Realiza transferência     |

---

# 🐞 Correção – Bug na Transferência

Foi identificada falha na regra de transferência que poderia gerar inconsistências.

## Problemas encontrados

* Não validava saldo suficiente
* Possibilidade de inconsistência em concorrência
* Ausência de rollback explícito

## Solução Implementada

* ✅ Validação de saldo insuficiente
* ✅ Retorno HTTP 409 (Conflict) quando inválido
* ✅ Execução transacional com rollback automático
* ✅ Implementação de **Optimistic Locking** via `@Version`
* ✅ Testes automatizados (Service e Controller)

O uso de `@Version` garante controle de concorrência e evita "lost update".

---

# 🧪 Testes Automatizados

Executar:

```bash
mvn test
```

Cobertura:

* Teste de transferência com saldo suficiente
* Teste de falha por saldo insuficiente
* Teste de retorno 409 no controller

---

# 💾 Banco de Dados

* H2 file database
* Criação automática via JPA
* Versionamento de entidade para controle de concorrência

---

# 🛠 Tecnologias Utilizadas

## Backend

* Spring Boot 3.2.x
* Spring Web
* Spring Data JPA
* H2 Database
* SpringDoc OpenAPI (Swagger)
* JUnit 5

## Frontend

* Angular 15
* Reactive Forms
* ngx-toastr
* Bootstrap

---

# 📌 Considerações Técnicas

* Arquitetura em camadas (Controller → Service → Repository)
* Separação clara de responsabilidades
* Tratamento adequado de exceções
* API documentada via OpenAPI
* Validação de regras de negócio no Service

---

# 👤 Autor

Projeto desenvolvido por Nicolli Almeida como solução para desafio técnico 

