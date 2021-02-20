import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Remark } from 'src/app/model/remark';

@Component({
  selector: 'app-encode-remark',
  templateUrl: './encode-remark.component.html',
  styleUrls: ['./encode-remark.component.css']
})
export class EncodeRemarkComponent implements OnInit {

  remarkEditionForm: FormGroup;
  
  constructor(public dialogRef: MatDialogRef<Remark>,
              @Inject(MAT_DIALOG_DATA) private _remark: Remark,
              private fb: FormBuilder) {
    this.remarkEditionForm = this.fb.group({
      message: [this._remark.message]
    })
  }

  ngOnInit(): void {
  }

  cancel(): void {
    this.dialogRef.close(null);
  }

  save(): void {
    this.dialogRef.close(this.remark);
  }

  get remark(): Remark {
    this._remark.message = this.remarkEditionForm.value['message'];

    return this._remark;
  }
}
