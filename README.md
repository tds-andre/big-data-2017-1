# Forecasting de Demanda usando Spark

Este repositório contem os fontes realizados para o trabalho de Big Data 2017/1, ministrada por Assis Bento. DEL, Escola Politécnica, UFRJ.

## Visão Geral
A solução contempla 3 componentes:

  - Um conjunto de script em Scala/Spark para fazer o tratamento de dados, treinar um modelo ARIMA(2,0,2) e carregar os dados no CouchDB
  - Uma view do CouchDB, necessária para WebApp
  - Um WebApp, que permite explorar os resultados.
    
## Conteúdo
### WebApp
Diretório **app** contém um Single Page Application, feito com HTML e Javascript puros. Utilizar NPM/Bower para dependências.

### CouchDB View
Diretório **couchdb-view**. Contém uma view (map & reduce) que deve ser instalada num banco do CouchDB antes para o WebApp poder acessar os dados.

### Spark Scripts
Diretório **spark-scala**. Contém os scripts em Scala que usam a API do Spark de RDD, DataFrame & ML. Os scripts estão nomeados e enumerados de acordo com a ordem de execução.

**Nota**: *não foi construindo ainda um App Spark em Scala. Os scripts foram feitos para ser rodados direto do spark-shell. Por exemplo:*
```sh
$ spark-shell
$ :load 1-setup.scala
```
**Nota**: *para rodar o script **4-load-couchdb.scala** é necessário instalar a dependência conforme abaixo:*
```sh
$ spark-shell --packages "org.scalaj:scalaj-http_2.11:2.3.0"
```
