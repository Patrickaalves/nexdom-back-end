# Inventory Control - Backend

Este é o backend do sistema de controle de estoque, desenvolvido em Java com Spring Boot.

## Visão Geral

Este projeto tem como objetivo principal fornecer uma API para gerenciar produtos, fornecedores, clientes e movimentações de estoque. Como o foco principal não era segurança e abstração de IDs, **os identificadores (UUIDs) foram expostos diretamente para o front-end** para facilitar a integração e acelerar o desenvolvimento.


---

## Tecnologias Utilizadas

- Java 24
- Spring Boot 3.5.0
- Spring Data JPA
- Spring Boot Starter Web
- Spring Boot Starter Validation
- PostgreSQL (via driver oficial)
- Specification Argument Resolver (`net.kaczmarzyk:specification-arg-resolver`) para filtros dinâmicos
- JUnit 5.11.0 (testes unitários)
- Mockito 5.11.0 (mocking e testes)
- Spring Boot Starter Test (testes integrados e utilitários)

---

## Configuração do Banco de Dados com Docker

Para facilitar o desenvolvimento, você pode subir um container PostgreSQL usando o comando abaixo:

`docker run -d --name postgres_nexdom_inventory_control -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=inventory_control -p 5433:5432 postgres`

## Clone do repositorio
git clone https://github.com/Patrickaalves/nexdom-back-end.git

