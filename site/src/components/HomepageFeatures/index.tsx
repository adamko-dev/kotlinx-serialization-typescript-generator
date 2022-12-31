import React from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import styles from './styles.module.css';

type FeatureItem = {
  buttonText: string;
  destination: string;
};

const FeatureList: FeatureItem[] = [
  {
    buttonText: 'Getting started',
    destination: "/docs/",
  },
  {
    buttonText: 'Examples',
    destination: "/docs/examples",
  },
];

function Feature({buttonText, destination}: FeatureItem) {
  return (
    <div className={clsx('col')}>
      <div className="text--center padding-horiz--md">
        <div className={styles.buttons}>
          <Link
            className="button button--secondary button--lg"
            to={destination}>
            {buttonText}
          </Link>
        </div>
      </div>
    </div>
  );
}

export default function HomepageFeatures(): JSX.Element {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row align-items-start">
          {FeatureList.map((props, idx) => (
            <Feature key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}
