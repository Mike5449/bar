import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterSearch'
})
export class FilterSearchPipe implements PipeTransform {

  transform(table: any, valueSearching: string ): any {

    let result: any;
    console.log(table)
    console.log(valueSearching)
    if (!table || !valueSearching) {
      return table;
    }
    result=table.filter((data:any)=>data?.employee?.firstName.toString().toLowerCase().includes(valueSearching.toLowerCase()))

    // if(type==='EMPLOYEE'){

    //  result=table.filter((data:any)=>data?.employee?.firstName.toString().toLowerCase().includes(valueSearching.toLowerCase()))


    // }else{

    //  result=table.filter((data:any)=>data?.name.toString().toLowerCase().includes(valueSearching.toLowerCase()))

    // }
    return result;
  }

}
