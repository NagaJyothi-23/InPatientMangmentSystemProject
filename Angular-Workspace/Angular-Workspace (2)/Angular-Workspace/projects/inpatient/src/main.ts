import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';


// // Proxy configuration
// const proxyUrl = 'http://localhost:8101'; // Backend server URL

// // Override XMLHttpRequest open method
// const originalXhrOpen = XMLHttpRequest.prototype.open;
// async:boolean=true
// XMLHttpRequest.prototype.open = function(this: XMLHttpRequest, method: string, url: string, async?: boolean | undefined , username?: string | null, password?: string | null) {
//   if (url.startsWith('/admin-service/')) {
//     url = proxyUrl + url;
//   }
//   return originalXhrOpen.call(this, method, url, async, username, password);
// };



platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));
