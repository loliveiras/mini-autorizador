# Mini-autorizador
Processamento para cadastro de cartões e transações de Vale Alimentação e Vale Refeição

# Passo a passo para expor a aplicação e realizar testes.

1 - Buildar aplicação com o comando: mvn clean install.

2 - Para gerar as imagens que irá subir no docker usar o comando na raiz do projeto onde esta o arquivo docker-compose.yml: docker-compose build

3 - para iniciar os containers do mongo e mini-autorizador usar o comando: docker build up -d

# Após subir a aplicação, terá o swagger disponível no seguinte endereço: http://localhost:8080/swagger-ui.html#


