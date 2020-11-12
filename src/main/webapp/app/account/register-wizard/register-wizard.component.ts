import { Component, AfterViewInit, ElementRef, ViewChild, SimpleChanges } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { JhiLanguageService } from 'ng-jhipster';
import * as $ from 'jquery';
import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/shared/constants/error.constants';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { RegisterService } from '../register/register.service';
import 'jquery-validation';
import 'bootstrap';
import 'twitter-bootstrap-wizard';
import 'bootstrap-select';

declare let swal: any;

interface FileReaderEventTarget extends EventTarget {
  result: string;
}
interface FileReaderEvent extends Event {
  target: FileReaderEventTarget;
  getMessage(): string;
}
interface JQuery {
  printArea(): void;
}

@Component({
  selector: 'jhi-register',
  templateUrl: './register-wizard.component.html',
  styleUrls: ['../../../content/scss/paper-dashboard.scss'],
})
export class RegisterWizardComponent implements AfterViewInit {
  @ViewChild('login', { static: false })
  login?: ElementRef;
  focus: any;
  focus1: any;
  focus2: any;
  files: any;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;

  registerForm = this.fb.group({
    login: [
      '',
      [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    ],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
  });

  constructor(
    private languageService: JhiLanguageService,
    private loginModalService: LoginModalService,
    private registerService: RegisterService,
    private fb: FormBuilder
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

  // eslint-disable-next-line @typescript-eslint/tslint/config
  ngOnInit(): void {
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

        // if($current == 1 || (mobile_device == true && (index % 2 == 0) )){
        //     move_distance -= 8;
        // } else if($current == total_steps || (mobile_device == true && (index % 2 == 1))){
        //     move_distance += 8;
        // }

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
  ngAfterViewInit(): void {
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
}
