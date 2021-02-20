import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable, NgZone } from '@angular/core';
import { MatSnackBar, MatSnackBarVerticalPosition } from '@angular/material/snack-bar';
import { EMPTY, Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MessageService, MessageType } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class HttpErrorInterceptorService implements HttpInterceptor {

  constructor(private messageService: MessageService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error : HttpErrorResponse) => this.handleError(error))
    );
  }

  private handleError(error : HttpErrorResponse): Observable<HttpEvent<any>> {
    if(error.error instanceof Error) {
      this.messageService.showMessage(error.error.message, MessageType.ERROR);
    }
    else if(error.error) {
      this.messageService.showMessage(error.statusText + ' ' + error.message, MessageType.ERROR);
    }

    return EMPTY;
  }
}
