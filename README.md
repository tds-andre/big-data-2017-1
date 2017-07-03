# Forecasting de Demanda usando Spark

Este reposit�rio contem os fontes realizados para o trabalho de Big Data 2017/1, ministrada por Assis Bento. DEL, Escola Polit�cnica, UFRJ.

## Vis�o Geral
A solu��o contempla 3 componentes:

  - Um conjunto de script em Scala/Spark para fazer o tratamento de dados, treinar um modelo ARIMA(2,0,2) e carregar os dados no CouchDB
  - Uma view do CouchDB, necess�ria para WebApp
  - Um WebApp, que permite explorar os resultados.
    
## Conte�do
### WebApp
Diret�rio **app** cont�m um Single Page Application, feito com HTML e Javascript puros. Utilizar NPM/Bower para depend�ncias.

### CouchDB View
Diret�rio **couchdb-view**. Cont�m uma view (map & reduce) que deve ser instalada num banco do CouchDB antes para o WebApp poder acessar os dados.

### Spark Scripts
Diret�rio **spark-scala**. Cont�m os scripts em Scala que usam a API do Spark de RDD, DataFrame & ML. Os scripts est�o nomeados e enumerados de acordo com a ordem de execu��o.

**Nota**: *n�o foi construindo ainda um App Spark em Scala. Os scripts foram feitos para ser rodados direto do spark-shell. Por exemplo:*
```sh
$ spark-shell
$ :load 1-setup.scala
```
**Nota**: *para rodar o script **4-load-couchdb.scala** � necess�rio instalar a depend�ncia conforme abaixo:*
```sh
$ spark-shell --packages "org.scalaj:scalaj-http_2.11:2.3.0"
```
