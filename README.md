# My Fitness Buddy - Android App

Esta é uma aplicação desenvolvida como projeto da unidade curricular de Programação de Aplicações Móveis, realizada pelos alunos:
- André Carvalho 202200878
- Diogo Veigas 202200879

## Versão SDK Utilizado

Esta aplicação, como uma esmagadoa maioria, suporta várias versões SDK, mas tendo o foco nas seguintes:
- Versão SDK mínima: *27*
- Versão SDK recomendada: *34*

## Versão do Gradle

Esta aplicação foi desenvolvida na versão do gradle 8.2, que pode ser verificado no ficheiro gradle-wrapper.properties, em que a versão da distribuição é a *8.2*:
```bash
distributionUrl=https\://services.gradle.org/distributions/gradle-8.2-bin.zip
```

## Discriminação das Bibliotecas Utilizadas

Neste projeto, os alunos não utilizaram qualquer tipo de biblioteca externa, ainda assim, serão descritas as bibliotecas utilizadas e o motivo pelo qual foram importadas.

### Bibliotecas de UI e Compatibilidade:

As bibliotecas abaixo servem para, basicamente, tornar o aspeto visual da apliação mais fluído e morderno.

- *AppCompat* (androidx.appcompat:appcompat:1.6.1):
    - Fornece recursos e funcionalidades compatíveis com versões mais antigas do Android, garantindo uma aparência e comportamento consistentes em diferentes versões do sistema operativo.

- *Material Design* (com.google.android.material:material:1.11.0):
  - Implementa os princípios do Material Design, oferecendo componentes de interface atraentes e modernos, como botões, barras, e outras componentes visuais que seguem as diretrizes de design do Google.

- *ConstraintLayout* (androidx.constraintlayout:constraintlayout:2.1.4):
  - Permite o uso do Constraint Layout, um layout bastante poderoso, flexível e moderno.

### Bibliotecas de Testes

Os alunos confessam não ter utilizado muito as bibliotecas de testes unitários, sendo que as mesmas são muito úteis, mas também precisam de mais tempo de desenvolvimento.

Estas incluem:
- *JUnit* (junit:junit:4.13.2);
- *JUnit para Android* (androidx.test.ext:junit:1.1.5);
- *Espresso* (androidx.test.espresso:espresso-core:3.5.1).

### Bibliotecas de Persistência de Dados

As bibliotecas de persistência de dados, isto é, Room, foram bastante utilizadas ao longo do projeto, sendo ela a principal atração de toda a aplicação:

- *Room* (androidx.room:room-runtime:2.4.1):
  - Biblioteca de persistência que simplifica o acesso a uma base de dados SQLite no Android. O Room fornece uma camada de abstração sobre o SQLite e permite definir facilmente esquemas de bases de dados utilizando anotações em classes Java/Kotlin.

- *Room Compiler* (androidx.room:room-compiler:2.4.1):
  - Processador de anotações usado pelo Room para gerar código relacionado a bases de dados SQLite durante a compilação. Isso inclui a geração de consultas SQL e o código para aceder à base de dados de maneira eficiente.