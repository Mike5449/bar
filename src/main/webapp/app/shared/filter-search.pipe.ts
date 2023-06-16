import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterSearch'
})
export class FilterSearchPipe implements PipeTransform {

  transform(table: any, valueSearching: string ,type:string ): any {

    let result: any;

    if (!table || !valueSearching) {
      return table;
    }
    result=table.filter((data:any)=>data?.employee?.firstName.toString().toLowerCase().includes(valueSearching.toLowerCase()))

    
    if(type==='EMPLOYEE'){

     result=table.filter((data:any)=>data?.employee?.firstName?.toString().toLowerCase().includes(valueSearching.toLowerCase()))


    }else if(type==='CLIENT'){

     result=table.filter((data:any)=>data?.name?.toString().toLowerCase().includes(valueSearching.toLowerCase()))
     console.group(table);
     console.group(valueSearching);
    }
    return result;
  }

}
