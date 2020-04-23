import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SyncifyTestModule } from '../../../test.module';
import { SongDetailComponent } from 'app/entities/song/song-detail.component';
import { Song } from 'app/shared/model/song.model';

describe('Component Tests', () => {
  describe('Song Management Detail Component', () => {
    let comp: SongDetailComponent;
    let fixture: ComponentFixture<SongDetailComponent>;
    const route = ({ data: of({ song: new Song(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SyncifyTestModule],
        declarations: [SongDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SongDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SongDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load song on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.song).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
