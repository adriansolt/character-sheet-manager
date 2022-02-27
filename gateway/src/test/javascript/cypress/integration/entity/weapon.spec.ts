import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Weapon e2e test', () => {
  const weaponPageUrl = '/weapon';
  const weaponPageUrlPattern = new RegExp('/weapon(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const weaponSample = { name: 'generating', weight: 59964, quality: 72922, reach: 34668, baseDamage: 55863, requiredST: 10812 };

  let weapon: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/weapons+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/weapons').as('postEntityRequest');
    cy.intercept('DELETE', '/api/weapons/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (weapon) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/weapons/${weapon.id}`,
      }).then(() => {
        weapon = undefined;
      });
    }
  });

  it('Weapons menu should load Weapons page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('weapon');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Weapon').should('exist');
    cy.url().should('match', weaponPageUrlPattern);
  });

  describe('Weapon page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(weaponPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Weapon page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/weapon/new$'));
        cy.getEntityCreateUpdateHeading('Weapon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weaponPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/weapons',
          body: weaponSample,
        }).then(({ body }) => {
          weapon = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/weapons+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/weapons?page=0&size=20>; rel="last",<http://localhost/api/weapons?page=0&size=20>; rel="first"',
              },
              body: [weapon],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(weaponPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Weapon page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('weapon');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weaponPageUrlPattern);
      });

      it('edit button click should load edit Weapon page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Weapon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weaponPageUrlPattern);
      });

      it('last delete button click should delete instance of Weapon', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('weapon').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weaponPageUrlPattern);

        weapon = undefined;
      });
    });
  });

  describe('new Weapon page', () => {
    beforeEach(() => {
      cy.visit(`${weaponPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Weapon');
    });

    it('should create an instance of Weapon', () => {
      cy.get(`[data-cy="name"]`).type('Cotton Bike SAS').should('have.value', 'Cotton Bike SAS');

      cy.get(`[data-cy="description"]`).type('Greece Handmade Administrator').should('have.value', 'Greece Handmade Administrator');

      cy.get(`[data-cy="weight"]`).type('67525').should('have.value', '67525');

      cy.get(`[data-cy="quality"]`).type('89300').should('have.value', '89300');

      cy.setFieldImageAsBytesOfEntity('picture', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="reach"]`).type('45647').should('have.value', '45647');

      cy.get(`[data-cy="baseDamage"]`).type('18567').should('have.value', '18567');

      cy.get(`[data-cy="requiredST"]`).type('46963').should('have.value', '46963');

      cy.get(`[data-cy="damageModifier"]`).type('43536').should('have.value', '43536');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        weapon = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', weaponPageUrlPattern);
    });
  });
});
