import { NgZone } from '@angular/core';
import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig, MatSnackBarVerticalPosition } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private dialog: MatSnackBar,
              private zone: NgZone) { }

  public showMessage(message: string, type: MessageType) {
    this.zone.run(() => {
      this.dialog.open(message, 'x', {
        duration: type == MessageType.ERROR ? 15000 : 3000,
        verticalPosition: 'top'
      });
    });
  }
}

export enum MessageType {
  INFORMATION,
  WARNING,
  ERROR
}
