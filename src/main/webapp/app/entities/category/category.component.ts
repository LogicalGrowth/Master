import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from './category.service';
import { CategoryDeleteDialogComponent } from './category-delete-dialog.component';

declare interface DataTable {
  headerRow: string[];
  footerRow: string[];
  dataRows: string[][];
}

@Component({
  selector: 'jhi-category',
  templateUrl: './category.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class CategoryComponent implements OnInit, OnDestroy {
  public dataTable: DataTable | undefined;
  categories?: ICategory[];
  eventSubscriber?: Subscription;

  constructor(protected categoryService: CategoryService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCategories();

    this.dataTable = {
      headerRow: ['Name', 'Position', 'Office', 'Age', 'Date', 'Actions'],
      footerRow: ['Name', 'Position', 'Office', 'Age', 'Start Date', 'Actions'],
      dataRows: [
        ['Airi Satou', 'Andrew Mike', 'Develop', '2013', '99,225', ''],
        ['Angelica Ramos', 'John Doe', 'Design', '2012', '89,241', 'btn-round'],
        ['Ashton Cox', 'Alex Mike', 'Design', '2010', '92,144', 'btn-simple'],
        ['Bradley Greer', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Brenden Wagner', 'Paul Dickens', 'Communication', '2015', '69,201', ''],
        ['Brielle Williamson', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Caesar Vance', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Cedric Kelly', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Charde Marshall', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Colleen Hurst', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Dai Rios', 'Andrew Mike', 'Develop', '2013', '99,225', ''],
        ['Doris Wilder', 'John Doe', 'Design', '2012', '89,241', 'btn-round'],
        ['Fiona Green', 'Alex Mike', 'Design', '2010', '92,144', 'btn-simple'],
        ['Garrett Winters', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Gavin Cortez', 'Paul Dickens', 'Communication', '2015', '69,201', ''],
        ['Gavin Joyce', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Gloria Little', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Haley Kennedy', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Herrod Chandler', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Hope Fuentes', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Howard Hatfield', 'Andrew Mike', 'Develop', '2013', '99,225', ''],
        ['Jena Gaines', 'John Doe', 'Design', '2012', '89,241', 'btn-round'],
        ['Jenette Caldwell', 'Alex Mike', 'Design', '2010', '92,144', 'btn-simple'],
        ['Jennifer Chang', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Martena Mccray', 'Paul Dickens', 'Communication', '2015', '69,201', ''],
        ['Michael Silva', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Michelle House', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Paul Byrd', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Prescott Bartlett', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Quinn Flynn', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Rhona Davidson', 'Andrew Mike', 'Develop', '2013', '99,225', ''],
        ['Shou Itou', 'John Doe', 'Design', '2012', '89,241', 'btn-round'],
        ['Sonya Frost', 'Alex Mike', 'Design', '2010', '92,144', 'btn-simple'],
        ['Suki Burks', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Tatyana Fitzpatrick', 'Paul Dickens', 'Communication', '2015', '69,201', ''],
        ['Tiger Nixon', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Timothy Mooney', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Unity Butler', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Vivian Harrell', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
        ['Yuri Berry', 'Mike Monday', 'Marketing', '2013', '49,990', 'btn-round'],
      ],
    };
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICategory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCategories(): void {
    this.eventSubscriber = this.eventManager.subscribe('categoryListModification', () => this.loadAll());
  }

  delete(category: ICategory): void {
    const modalRef = this.modalService.open(CategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.category = category;
  }
}
