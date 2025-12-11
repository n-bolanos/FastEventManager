# Fast Event Manager Web FrontEnd

Fast Event Manager (FEM) Web Frontend is a Vue.js SPA used for managing events, registering attendees, and confirming attendance through public links. It communicates with several microservices through an API gateway.

## Tech Stack

- Vue 3 (Composition API)

- Pinia (global store)

- Vite (bundler)

- TailwindCSS (styling)

- Axios (HTTP client)

- Nginx (production static hosting)

- Docker (build + deploy)

## Project Setup
Make sure you have node js installed, the version used to create this project was v25.2.1 and NPM's 11.6.2.

### Clone the github repo
```sh
git clone https://github.com/n-bolanos/FastEventManager.git
```

### CD
```sh
cd FastEventManager\src\web-frontend\FEM_FRONT
```

### Set the env variable
```cmd
set VITE_API_GATEWAY_URL=http://localhost:8010
```

### Install the project dependencies
```sh
npm install
```
This command install all the dependencies needed for the project, this step result in a node_modules folder.

### Compile and Hot-Reload for Development

```sh
npm run dev
```
This command will start the development server so you can check the app locally, the app will be running on the localhost:8050 port.

### Note.
If you want to try the whole app run all the microservices separately.

## Front Structure
- src/
  - main.js — app bootstrap (VUE, Pinia, router, Toast) mount the hole app.
  - App.vue — root component - enables the routing for the SPA.
  - style.css — global styles - starts tailwind.
  - router/index.js — routes configuration.
  - stores/auth.js — Pinia store for auth (tokens, logout).
  - service/api_gateway.js — axios central client (add tokens, manage refresh).
  - js/
    - Attendance.js — attendance related functionalities (confirm, create/update).
    - Authentication.js — login and user register.
    - Event.js — creation, list, share and event deletion.
  - components/
    - AttendanceForm.vue — attendance confirmation/updating form.
    - Create.vue — event creation form.
    - Document.vue — id input form.
    - Events.vue — principal event management panel.
    - LoginForm.vue — login form.
    - NavBar.vue — upper bar with titles an logout.
    - Register.vue — single register element to show attendances.
    - SignUp.vue — user register form.
    - SingleEvent.vue — event singular card (actions: delete, share and see details).
  - icons/ — SVG icons.
  - views/
    - main.vue — main page (Dashboard: Auth, Events, Create).
    - attendance.vue — public vew to confirm attendance to an event.
  - assets/images/ — images used by UI.

- public/ — favicon icon.
- package.json — scripts and dependencies.
- vite.config.js — Vite config.
- Dockerfile — production image builder (copy dist to nginx).
- nginx.conf — static server config.