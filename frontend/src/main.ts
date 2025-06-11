import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { APP_INITIALIZER, importProvidersFrom } from '@angular/core';
import { AppComponent } from './app/app.component';
import { ApiService } from './app/service/api.service';
import { firstValueFrom } from 'rxjs';
import { routes } from './app/app.routes';

export function initApp(apiService: ApiService) {
  return () => firstValueFrom(apiService.checkAuthStatus());
}

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(),
    provideRouter(routes),

    ApiService,
    {
      provide: APP_INITIALIZER,
      useFactory: initApp,
      deps: [ApiService],
      multi: true
    }
  ]
}).catch(err => console.error(err));
