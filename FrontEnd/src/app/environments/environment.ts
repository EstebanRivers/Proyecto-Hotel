export const localhost: string = 'http://localhost';

export const environment = {
    apiUrl: localhost.concat(':8090/api/'),
    authUrl: localhost.concat(':8090/api/login'),
    apiUsuarios: localhost.concat(':8090/api/usuarios')
}