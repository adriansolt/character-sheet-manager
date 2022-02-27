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

describe('Campaign e2e test', () => {
  const campaignPageUrl = '/campaign';
  const campaignPageUrlPattern = new RegExp('/campaign(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const campaignSample = { name: 'Lilangeni', map: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=', mapContentType: 'unknown', masterId: 64556 };

  let campaign: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/campaigns+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/campaigns').as('postEntityRequest');
    cy.intercept('DELETE', '/api/campaigns/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (campaign) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/campaigns/${campaign.id}`,
      }).then(() => {
        campaign = undefined;
      });
    }
  });

  it('Campaigns menu should load Campaigns page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('campaign');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Campaign').should('exist');
    cy.url().should('match', campaignPageUrlPattern);
  });

  describe('Campaign page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(campaignPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Campaign page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/campaign/new$'));
        cy.getEntityCreateUpdateHeading('Campaign');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', campaignPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/campaigns',
          body: campaignSample,
        }).then(({ body }) => {
          campaign = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/campaigns+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/campaigns?page=0&size=20>; rel="last",<http://localhost/api/campaigns?page=0&size=20>; rel="first"',
              },
              body: [campaign],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(campaignPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Campaign page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('campaign');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', campaignPageUrlPattern);
      });

      it('edit button click should load edit Campaign page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Campaign');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', campaignPageUrlPattern);
      });

      it('last delete button click should delete instance of Campaign', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('campaign').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', campaignPageUrlPattern);

        campaign = undefined;
      });
    });
  });

  describe('new Campaign page', () => {
    beforeEach(() => {
      cy.visit(`${campaignPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Campaign');
    });

    it('should create an instance of Campaign', () => {
      cy.get(`[data-cy="name"]`).type('Michigan').should('have.value', 'Michigan');

      cy.get(`[data-cy="description"]`).type('scale').should('have.value', 'scale');

      cy.setFieldImageAsBytesOfEntity('map', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="masterId"]`).type('58062').should('have.value', '58062');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        campaign = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', campaignPageUrlPattern);
    });
  });
});
