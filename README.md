<p align="center">
    <img width="50%" src="https://github.com/RepGraph/RepGraph/blob/main/RGLogo.png"> 
</p>

# RepGraph

RepGraph is an application tool to equip Natural Language Processing (NLP) Researchers and Engineers with the ability to visualise and analyse graph data that has been processed by one of the MRP frameworks. [Demonstration Video](https://vimeo.com/user136369092/repgraph) - [Demonstration Website](https://repgraph.vercel.app/)

**Last Updated March 2021**

## Frameworks Supported
* DMRS
* EDS
* PTG
* UCCA
* AMR

## Built With
* React
* React visx
* Material UI
* Spring Boot
* Java

## Contents
- [RepGraph](#RepGraph)
    - [Contents](#Contents)
    - [Usage](#Usage)
    - [Getting started](#Getting-Started)
    - [Repository structure](#Repository-Structure)
    - [Contribution guidelines](#Contribution-Guidelines)


## Usage
The user interface is easy to use and intuitive but for a clear and easy usage guide, refer to the user manual here: [User Manual](https://github.com/EdanToledo/RepGraph/blob/main/UserManual.pdf)

If you simply want to use the application without the hassle of compiling and running everything locally - you can go [here](https://repgraph.vercel.app/)

For access to sample data as well as a description of Uniform Graph Interchange Format: http://mrp.nlpl.eu/2020/index.php?page=14

## Getting Started
To use the RepGraph tool is simple. All that is needed are up to date versions of the following prerequisites and Language compilers/interpreters.

* To access source code, simply clone the RepGraph repository to your local file system:

```commandline
git clone https://github.com/RepGraph/RepGraph.git
```

## Installation
### Prerequisites
* Java
* Maven
* Node Package Manager
* Python 3.8.5

### Backend

```bash
pip install -r requirements.txt
mvn compile
mvn package
java -jar *Location of Jar File*
```
### Frontend

```bash
npm install
npm start
```

Remember to change the API endpoint in the AppContextProvider file if you want to use a locally hosted backend.
## Repository structure

```commandline
RepGraph
├── src 'contains all source code for the backend and frontend'
|   ├── main 'contains source code for the repgraph tool'
|       ├── java 'contains all java source code that is used in backend'
|       ├── javascript 'contains all javascript code that is used in frontend'
|       └── resources 'contains all other resources used such as config and property files'
```

## Acknowledgements

A big thanks to Jan Buys (jbuys@cs.uct.ac.za) for his help and supervision of this project.

## Authors
* Edan Toledo
* Roy Cohen
* Jaron Cohen
* Jan Buys

## Contact
* TLDEDA001@myuct.ac.za
* CHNROY002@myuct.ac.za
* CHNJAR003@myuct.ac.za
* jbuys@cs.uct.ac.za

## Contribution guidelines
Please ensure that you adhere to the following guidelines when making any contributions to this repo:

- First create an issue describing the feature, improvement or bug fix. Issue wording should be clear and easy to understand.
- Create a new branch for the specific issue and prefix your branch name with the issue number followed by a few words describing the issue (e.g. `34-dark-mode`).
- Please also ensure all paths are relative and not hard coded
- For all newly created functions, please make sure to include docs
- For all newly created functions, include corresponding unit tests in the `test` folder.
- Commit any changes to your branch with an appropriate message.
- Before creating a new merge request, ensure that your commit passes all unit tests.
- Create a new merge request. Your contribution will not be accepted unless:
    1. all new functions have docs,
    2. the appropriate unit tests have been created,
    3. and your code passes all unit tests.
- Once a core member has reviewed and approved with the changes/additions you have made, your branch will be merged with `master` and will be live in the RepGraph tool.
- Please make sure old branches are deleted after they have been merged and are no longer in use.




