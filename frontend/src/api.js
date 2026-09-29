const API = 'http://localhost:8080/api';

export async function request(path, options = {}) {
  const response = await fetch(API + path, {
    headers: {'Content-Type': 'application/json', ...(options.headers || {})},
    ...options
  });

  if (!response.ok) {
    let message = 'Request failed';
    try {
      const data = await response.json();
      message = data.message || message;
    } catch {}
    throw new Error(message);
  }
  return response.json();
}

export { API };
