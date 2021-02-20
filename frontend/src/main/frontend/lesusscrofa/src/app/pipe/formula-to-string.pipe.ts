import { Pipe, PipeTransform } from '@angular/core';
import { Formula, getFormulaName } from '../model/formula';

@Pipe({
  name: 'formulaToString'
})
export class FormulaToStringPipe implements PipeTransform {

  transform(value: Formula): string {
    return getFormulaName(value);
  }

}
