import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'lastUpdate',
})
export class LastUpdatePipe implements PipeTransform {
  transform(value?: number): string {
    let output = '';
    switch (value) {
      case 0: {
        output = 'Hace unas horas';
        break;
      }
      default: {
        output = 'Hace ' + value + ' días';
        break;
      }
    }
    return output;
  }
}
