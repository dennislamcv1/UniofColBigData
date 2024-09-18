import http from 'k6/http';
import {check} from 'k6';



export const options = {
  stages: [
    { duration: '30s', target: 10 },
  ]
}

let url = "http://localhost:8081/request-registration"
let body = JSON.stringify({email: "test@example.com"})
let prams = {headers: {'Content-Type': 'application/json'}}

export default function () {
  let res = http.post(url, body, prams);
  check(res, {
    'is status 204': (r) => r.status === 204,
  });
}

