import { Component, AfterViewInit, ElementRef, ViewChild, SimpleChanges } from '@angular/core';
import { Validators, FormBuilder } from '@angular/forms';
import { ICategory } from 'app/shared/model/category.model';
import { JhiLanguageService } from 'ng-jhipster';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { RegisterService } from '../register/register.service';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { CategoryService } from 'app/entities/category/category.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IApplicationUser, ApplicationUser } from 'app/shared/model/application-user.model';
import { IdType } from 'app/shared/model/enumerations/id-type.model';
import * as moment from 'moment';
import { DATE_TIME_FORMAT, DATE_FORMAT } from 'app/shared/constants/input.constants';
import { LOGIN_ALREADY_USED_TYPE, EMAIL_ALREADY_USED_TYPE } from 'app/shared/constants/error.constants';
import { AccountService } from 'app/core/auth/account.service';
import { User } from 'app/core/user/user.model';

declare let swal: any;

interface FileReaderEventTarget extends EventTarget {
  result: string;
}
interface FileReaderEvent extends Event {
  target: FileReaderEventTarget;
  getMessage(): string;
}

@Component({
  selector: 'jhi-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class UserProfileComponent implements AfterViewInit {
  @ViewChild('login', { static: false })
  isSaving = false;
  login?: ElementRef;
  focus: any;
  focus1: any;
  focus2: any;
  focus3: any;
  files: any;
  categories?: ICategory[];
  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;
  account!: User;
  applicationUser!: ApplicationUser[];

  registerForm = this.fb.group({
    firstname: [],
    lastname: [],
    email: [],
    password: [],
    identify: [null, [Validators.required]],
    phone: [null],
    admin: [],
    birthdate: [null],
    image: [],
  });
  imageSrc: any;

  constructor(
    private languageService: JhiLanguageService,
    private loginModalService: LoginModalService,
    private registerService: RegisterService,
    private fb: FormBuilder,
    private loginApplicationUser: ApplicationUserService,
    private categoryService: CategoryService,
    private accountService: AccountService
  ) {}

  readURL(input: any): void {
    if (input.files && input.files[0]) {
      const reader: any = new FileReader();

      reader.onload = (e: FileReaderEvent) => {
        $('#wizardPicturePreview').attr('src', e.target.result).fadeIn('slow');
      };
      reader.readAsDataURL(input.files[0]);
    }
  }

  loadAll(): void {
    this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
  }

  // eslint-disable-next-line @typescript-eslint/tslint/config
  ngOnInit(): void {
    this.loadAll();

    if (this.login) {
      this.login.nativeElement.focus();
    }

    setTimeout(() => {
      $('.card.card-wizard').addClass('active');
    }, 600);
    if ($('.selectpicker').length !== 0) {
      $('.selectpicker').selectpicker({
        iconBase: 'nc-icon',
        tickIcon: 'nc-check-2',
      });
    }

    $.validator.addMethod('checkphonenumber', function (value): any {
      return /[0-9]/.test(value);
    });

    $.validator.addMethod('checkpassword', function (value): any {
      return /((?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\W]).{6,64})/.test(value);
    });

    // Code for the Validator
    const $validator = $('.card-wizard form').validate({
      rules: {
        firstname: {
          required: true,
          minlength: 3,
        },
        lastname: {
          required: true,
          minlength: 3,
        },
        email: {
          required: true,
          minlength: 3,
          email: true,
        },
        identify: {
          required: true,
          minlength: 13,
          maxlength: 13,
        },
        phone: {
          required: true,
          minlength: 8,
          checkphonenumber: true,
        },
      },
      messages: {
        email: {
          required: 'this field is required',
          minlength: 'this field must contain at least {0} characters',
          digits: 'this field can only contain numbers',
        },
      },

      highlight(element: any): void {
        $(element).closest('.form-group').removeClass('has-success').addClass('has-danger');
      },
      success(element: any): void {
        $(element).closest('.form-group').removeClass('has-danger').addClass('has-success');
      },
      errorPlacement(error: any, element: any): void {
        $(element).append(error);
      },
    });

    // Wizard Initialization
    const wizardCard = $('.card-wizard') as any;
    wizardCard.bootstrapWizard({
      tabClass: 'nav nav-pills',
      nextSelector: '.btn-next',
      previousSelector: '.btn-previous',

      onNext(): any {
        const $valid = ($('.card-wizard form') as any).valid();
        if (!$valid) {
          $validator.focusInvalid();
          return false;
        }
      },

      onInit(tab: any, navigation: any, index: any): any {
        // check number of tabs and fill the entire row
        let $total = navigation.find('li').length;
        const $wizard = navigation.closest('.card-wizard');

        const $firstLi = navigation.find('li:first-child a').html();
        const $movingDiv = $('<div class="moving-tab">' + $firstLi + '</div>');
        $('.card-wizard .wizard-navigation').append($movingDiv);

        $total = $wizard.find('.nav li').length;
        let $liWidth = 100 / $total;

        const totalSteps = $wizard.find('.nav li').length;
        let moveDistance = $wizard.width() / totalSteps;
        let indexTemp = index;
        let verticalLevel = 0;

        const mobileDevice = ($(document).width() as any) < 600 && $total > 3;

        if (mobileDevice) {
          moveDistance = $wizard.width() / 2;
          indexTemp = index % 2;
          $liWidth = 50;
        }

        $wizard.find('.nav li').css('width', $liWidth + '%');

        const stepWidth = moveDistance;
        moveDistance = moveDistance * indexTemp;

        const $current = index + 1;

        if ($current === 1 || (mobileDevice === true && index % 2 === 0)) {
          moveDistance -= 8;
        } else if ($current === totalSteps || (mobileDevice === true && index % 2 === 1)) {
          moveDistance += 8;
        }

        if (mobileDevice) {
          const x: any = index / 2;
          verticalLevel = parseInt(x, 10);
          verticalLevel = verticalLevel * 38;
        }

        $wizard.find('.moving-tab').css('width', stepWidth);
        $('.moving-tab').css({
          transform: 'translate3d(' + moveDistance + 'px, ' + verticalLevel + 'px, 0)',
          transition: 'all 0.5s cubic-bezier(0.29, 1.42, 0.79, 1)',
        });
        $('.moving-tab').css('transition', 'transform 0s');
      },

      onTabClick(): boolean {
        const $valid = ($('.card-wizard form') as any).valid();

        if (!$valid) {
          return false;
        } else {
          return true;
        }
      },

      onTabShow(tab: any, navigation: any, index: any): void {
        let $total = navigation.find('li').length;
        let $current = index + 1;

        const $wizard = navigation.closest('.card-wizard');

        // If it's the last tab then hide the last button and show the finish instead
        if ($current >= $total) {
          $($wizard).find('.btn-next').hide();
          $($wizard).find('.btn-finish').show();
        } else {
          $($wizard).find('.btn-next').show();
          $($wizard).find('.btn-finish').hide();
        }

        const buttonText = navigation.find('li:nth-child(' + $current + ') a').html();

        setTimeout(() => {
          $('.moving-tab').html(buttonText);
        }, 150);

        const checkbox = $('.footer-checkbox');

        if (index === 0) {
          $(checkbox).css({
            opacity: '0',
            visibility: 'hidden',
            position: 'absolute',
          });
        } else {
          $(checkbox).css({
            opacity: '1',
            visibility: 'visible',
          });
        }

        $total = $wizard.find('.nav li').length;
        let $liWidth = 100 / $total;

        const totalSteps = $wizard.find('.nav li').length;
        let moveDistance = $wizard.width() / totalSteps;
        let indexTemp = index;
        let verticalLevel = 0;

        const mobileDevice = ($(document).width() as any) < 600 && $total > 3;

        if (mobileDevice) {
          moveDistance = $wizard.width() / 2;
          indexTemp = index % 2;
          $liWidth = 50;
        }

        $wizard.find('.nav li').css('width', $liWidth + '%');

        const stepWidth = moveDistance;
        moveDistance = moveDistance * indexTemp;

        $current = index + 1;

        if (mobileDevice) {
          const x: any = index / 2;
          verticalLevel = parseInt(x, 10);
          verticalLevel = verticalLevel * 38;
        }

        $wizard.find('.moving-tab').css('width', stepWidth);
        $('.moving-tab').css({
          transform: 'translate3d(' + moveDistance + 'px, ' + verticalLevel + 'px, 0)',
          transition: 'all 0.5s cubic-bezier(0.29, 1.42, 0.79, 1)',
        });
      },
    });

    // Prepare the preview for profile picture
    $('#wizard-picture').change(() => {
      const input = $('#wizard-picture') as any;

      if (input[0].files && input[0].files[0]) {
        const reader: any = new FileReader();

        reader.onload = (e: FileReaderEvent) => {
          $('#wizardPicturePreview').attr('src', e.target.result).fadeIn('slow');
        };
        reader.readAsDataURL(input[0].files[0]);
      }
    });

    $('[data-toggle="wizard-radio"]').click(() => {
      const wizard = $(this).closest('.card-wizard');
      wizard.find('[data-toggle="wizard-radio"]').removeClass('active');
      $(this).addClass('active');
      $(wizard).find('[type="radio"]').removeAttr('checked');
      $(this).find('[type="radio"]').attr('checked', 'true');
    });

    $('[data-toggle="wizard-checkbox"]').click(() => {
      if ($(this).hasClass('active')) {
        $(this).removeClass('active');
        $(this).find('[type="checkbox"]').removeAttr('checked');
      } else {
        $(this).addClass('active');
        $(this).find('[type="checkbox"]').attr('checked', 'true');
      }
    });

    $('.set-full-height').css('height', 'auto');
  }

  saveAplicationUser(): void {
    const userApplicationFormObj = this.mappingDataFromForm();
    this.subscribeToSaveResponse(this.loginApplicationUser.update(userApplicationFormObj));
  }

  saveUser(): void {}

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  previousState(): void {
    window.history.back();
  }

  private mappingDataFromForm(): IApplicationUser {
    return {
      ...new ApplicationUser(),
      id: this.applicationUser[0].id,
      identification: this.registerForm.get(['identify'])!.value,
      phoneNumber: this.registerForm.get(['phone'])!.value,
      admin: false,
      idType: IdType.IDENTIFICATION,
      birthDate: moment(this.registerForm.get(['birthdate'])!.value, DATE_TIME_FORMAT),
      internalUser: {
        id: this.applicationUser[0].internalUser?.id,
        email: this.registerForm.get(['email'])!.value,
        firstName: this.registerForm.get(['firstname'])!.value,
        lastName: this.registerForm.get(['lastname'])!.value,
        activated: false,
        login: this.registerForm.get(['email'])!.value,
        imageurl: this.imageSrc,
      },
    };
  }

  register(): void {
    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const password = this.registerForm.get(['password'])!.value;
    if (password !== this.registerForm.get(['confirmPassword'])!.value) {
      this.doNotMatch = true;
    } else {
      const login = this.registerForm.get(['login'])!.value;
      const email = this.registerForm.get(['email'])!.value;
      this.registerService.save({ login, email, password, langKey: this.languageService.getCurrentLanguage() }).subscribe(
        () => (this.success = true),
        response => this.processError(response)
      );
    }
  }

  openLogin(): void {
    this.loginModalService.open();
  }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }

  onFinishWizard(): void {
    swal('Good job!', 'You clicked the finish button!', 'success');
  }

  // eslint-disable-next-line @typescript-eslint/adjacent-overload-signatures
  updateForm(aplicationUser: IApplicationUser): void {
    this.registerForm.patchValue({
      firstname: aplicationUser.internalUser?.firstName,
      lastname: aplicationUser.internalUser?.lastName,
      email: aplicationUser.internalUser?.email,
      identify: aplicationUser.identification,
      phone: aplicationUser.phoneNumber,
      birthdate: aplicationUser.birthDate ? aplicationUser.birthDate.format(DATE_FORMAT) : null,
      image: aplicationUser.internalUser?.imageurl,
      password: aplicationUser.internalUser?.password,
    });
    this.imageSrc = aplicationUser.internalUser?.imageurl;
  }

  ngAfterViewInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.account = account;
        this.loginApplicationUser.query({ 'internalUserId.equals': this.account.id }).subscribe((res: HttpResponse<IApplicationUser[]>) => {
          this.applicationUser = res.body || [];
          this.updateForm(this.applicationUser[0]);
        });
      }
    });

    $(window).resize(() => {
      $('.card-wizard').each((_, elementCard) => {
        const $wizard = $(elementCard);
        const index = ($wizard as any).bootstrapWizard('currentIndex');
        const $total = $wizard.find('.nav li').length;
        // eslint-disable-next-line @typescript-eslint/camelcase
        let $liWidth = 100 / $total;

        const totalSteps = $wizard.find('.nav li').length;
        let moveDistance = ($wizard.width() as any) / totalSteps;
        let indexTemp = index;
        let verticalLevel = 0;

        const mobileDevice = ($(document).width() as any) < 600 && $total > 3;

        if (mobileDevice) {
          moveDistance = ($wizard.width() as any) / 2;
          indexTemp = index % 2;
          $liWidth = 50;
        }

        $wizard.find('.nav li').css('width', $liWidth + '%');

        const stepWidth = moveDistance;
        moveDistance = moveDistance * indexTemp;

        const $current = index + 1;

        if ($current === 1 || (mobileDevice === true && index % 2 === 0)) {
          moveDistance -= 8;
        } else if ($current === totalSteps || (mobileDevice === true && index % 2 === 1)) {
          moveDistance += 8;
        }

        if (mobileDevice) {
          const x: any = index / 2;
          verticalLevel = parseInt(x, 10);
          verticalLevel = verticalLevel * 38;
        }

        $wizard.find('.moving-tab').css('width', stepWidth);
        $('.moving-tab').css({
          transform: 'translate3d(' + moveDistance + 'px, ' + verticalLevel + 'px, 0)',
          transition: 'all 0.5s cubic-bezier(0.29, 1.42, 0.79, 1)',
        });

        $('.moving-tab').css({
          transition: 'transform 0s',
        });
      });
    });
  }

  // eslint-disable-next-line @typescript-eslint/tslint/config
  ngOnChanges(changes: SimpleChanges): void {
    const input = $(this);

    if (input[0].files && input[0].files[0]) {
      const reader: any = new FileReader();

      reader.onload = (e: FileReaderEvent) => {
        $('#wizardPicturePreview').attr('src', e.target.result).fadeIn('slow');
      };
      reader.readAsDataURL(input[0].files[0]);
    }
  }

  saveImage(data: any): void {
    this.imageSrc = data.secure_url;
  }
}
