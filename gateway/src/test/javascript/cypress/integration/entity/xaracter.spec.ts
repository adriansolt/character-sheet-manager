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

describe('Xaracter e2e test', () => {
  const xaracterPageUrl = '/xaracter';
  const xaracterPageUrlPattern = new RegExp('/xaracter(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const xaracterSample = { name: 'compressing synthesizing', weight: 40675, height: 25435, points: 54283 };

  let xaracter: any;
  //let user: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"id":"d37ea2fb-a7a0-4a0e-bab3-a6ab045fc689","login":"Cotton 1080p","firstName":"Jerod","lastName":"Bahringer"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/xaracters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/xaracters').as('postEntityRequest');
    cy.intercept('DELETE', '/api/xaracters/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/notes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/xaracter-attributes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/xaracter-skills', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (xaracter) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/xaracters/${xaracter.id}`,
      }).then(() => {
        xaracter = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('Xaracters menu should load Xaracters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('xaracter');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Xaracter').should('exist');
    cy.url().should('match', xaracterPageUrlPattern);
  });

  describe('Xaracter page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(xaracterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Xaracter page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/xaracter/new$'));
        cy.getEntityCreateUpdateHeading('Xaracter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/xaracters',
          body: {
            ...xaracterSample,
            user: user,
          },
        }).then(({ body }) => {
          xaracter = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/xaracters+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/xaracters?page=0&size=20>; rel="last",<http://localhost/api/xaracters?page=0&size=20>; rel="first"',
              },
              body: [xaracter],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(xaracterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(xaracterPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Xaracter page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('xaracter');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterPageUrlPattern);
      });

      it('edit button click should load edit Xaracter page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Xaracter');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Xaracter', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('xaracter').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterPageUrlPattern);

        xaracter = undefined;
      });
    });
  });

  describe('new Xaracter page', () => {
    beforeEach(() => {
      cy.visit(`${xaracterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Xaracter');
    });

    it.skip('should create an instance of Xaracter', () => {
      cy.get(`[data-cy="name"]`).type('Director Account bandwidth').should('have.value', 'Director Account bandwidth');

      cy.get(`[data-cy="weight"]`).type('62138').should('have.value', '62138');

      cy.get(`[data-cy="height"]`).type('72655').should('have.value', '72655');

      cy.get(`[data-cy="points"]`).type('65026').should('have.value', '65026');

      cy.setFieldImageAsBytesOfEntity('picture', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="handedness"]`).select('RIGHT');

      cy.get(`[data-cy="campaignId"]`).type('10992').should('have.value', '10992');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click().should('be.checked');

      cy.get(`[data-cy="user"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        xaracter = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', xaracterPageUrlPattern);
    });
  });
});
