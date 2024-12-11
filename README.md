# Mini Autorizador

Este é o projeto Mini Autorizador, uma aplicação simples para gerenciar registros e transações de cartões.

## Funcionalidades

- Registro de cartões
- Consulta de saldo do cartão
- Transação de cartões

## Tecnologias Utilizadas

- Java
- Spring Boot
- MongoDB, Mongo Express
- Docker

## Como Executar

1. Clone o repositório através do link:
    git clone (https://github.com/loliveiras/mini-autorizador.git)

2. Navegue até o diretório do projeto:

    cd miniautorizador

3. Execute o build da aplicação:

    mvn clean install

4. Execute o build do arquivo docker compose

    docker compose build

5. Execute o starting da aplicação

    docker compose up -d

6. Após finalizar, verifique se a aplicação subiu de forma completa.

    docker ps

## Testes Funcionais no Postman

[Para garantir que a aplicação está funcionando corretamente, você pode executar testes funcionais utilizando o Postman.]

1. Importe a coleção de testes do Postman disponível no repositório - /mini-autorizador/postman/MINI AUTORIZADOR.postman_collection.json.
2. Abra o Postman e navegue até a aba "Collections".
3. Clique em "Import" e selecione o arquivo da coleção de testes.
4. Configure o ambiente de teste, se necessário.
5. Execute a coleção de testes para verificar se todas as funcionalidades estão operando conforme esperado.

[OBS: Utilizar os contratos disponíveis no link após start da aplicação:] (http://localhost:8080/swagger-ui.html#/cartoes-controller)

## Informações adicionais
[Após realizar os testes é possível conferir os dados registrados na collection: gerenciamento_cartoes através do link: (http://localhost:8081/)]