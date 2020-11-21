import { Pipe, PipeTransform } from '@angular/core';
import { ActivityStatus } from 'app/shared/model/enumerations/activity-status.model';

@Pipe({
  name: 'state',
})
export class StatePipe implements PipeTransform {
  transform(value?: ActivityStatus): string {
    let output = '';
    switch (value) {
      case 'ENABLED': {
        output = 'Habilitado';
        break;
      }
      case 'DISABLED': {
        output = 'Deshabilitado';
        break;
      }
      case 'FINISHED': {
        output = 'Terminado';
        break;
      }
      default: {
        output = 'Indefinido';
        break;
      }
    }
    return output;
  }
}
