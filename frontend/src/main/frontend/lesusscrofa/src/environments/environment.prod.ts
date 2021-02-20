export const environment = {
  production: true,
  apiUrl: getBaseUrl() + "api"
};

export function getBaseUrl() {
  return document.getElementsByTagName('base')[0].href;
};